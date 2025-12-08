package com.example.llmbackend.service;

import com.example.llmbackend.dto.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Service
public class LLMService {

    private final RestTemplate restTemplate;

    @Value("${python.service.url:http://localhost:8000}")
    private String pythonServiceUrl;

    public LLMService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SurveyResponse generateSurvey(SurveyRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            System.out.println("🔄 Generating survey for theme: " + request.getTheme());

            // Prepare request for Python service
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("theme", request.getTheme());
            requestBody.put("questionCount", request.getQuestionCount());
            requestBody.put("answersPerQuestion", request.getAnswersPerQuestion());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Call Python LLM service
            ResponseEntity<SurveyResponse> response = restTemplate.exchange(
                    pythonServiceUrl + "/generate-survey",
                    HttpMethod.POST,
                    entity,
                    SurveyResponse.class
            );

            long processingTime = System.currentTimeMillis() - startTime;
            SurveyResponse surveyResponse = response.getBody();

            if (surveyResponse != null) {
                surveyResponse.setProvider("python-llm-service");
                surveyResponse.setProcessingTimeMs(processingTime);
            }

            System.out.println("✅ Successfully generated survey in " + processingTime + "ms");
            return surveyResponse;

        } catch (ResourceAccessException e) {
            long processingTime = System.currentTimeMillis() - startTime;
            System.err.println("❌ Python service timeout: " + e.getMessage());

            SurveyResponse errorResponse = new SurveyResponse();
            errorResponse.setErrorMessage("LLM service timeout - please try again later");
            errorResponse.setProcessingTimeMs(processingTime);
            return errorResponse;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            long processingTime = System.currentTimeMillis() - startTime;
            System.err.println("❌ Python service error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());

            SurveyResponse errorResponse = new SurveyResponse();
            errorResponse.setErrorMessage("LLM service error: " + e.getStatusCode());
            errorResponse.setProcessingTimeMs(processingTime);
            return errorResponse;

        } catch (Exception e) {
            long processingTime = System.currentTimeMillis() - startTime;
            System.err.println("❌ Unexpected error: " + e.getMessage());

            SurveyResponse errorResponse = new SurveyResponse();
            errorResponse.setErrorMessage("Internal server error");
            errorResponse.setProcessingTimeMs(processingTime);
            return errorResponse;
        }
    }

    public boolean isOllamaHealthy() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    "http://localhost:11434/api/tags", String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPythonServiceHealthy() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    pythonServiceUrl + "/health", String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

    public ModelInfo getModelInfo() {
        try {
            // In a real implementation, you would parse the actual response
            ModelInfo info = new ModelInfo();
            info.setOllamaVersion("1.0.0");
            info.setModel("mistral:7b");
            info.setStatus(isOllamaHealthy() ? "LOADED" : "NOT_LOADED");
            info.setGpuAvailable(true);

            return info;
        } catch (Exception e) {
            return new ModelInfo("UNKNOWN", "Unknown", "UNAVAILABLE", false);
        }
    }
}