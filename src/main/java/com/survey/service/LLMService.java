package com.survey.service;

import com.survey.dto.AIGenerateSurveyRequest;
import com.survey.dto.AIGenerateSurveyResponse;
import com.survey.dto.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LLM Service
 * Integrates with Python LLM service for AI-powered survey generation
 */
@Service
public class LLMService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${llm.service.url}")
    private String llmServiceUrl;

    /**
     * Generate survey questions using AI (LLM)
     */
    public AIGenerateSurveyResponse generateSurvey(AIGenerateSurveyRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            System.out.println("🤖 Generating survey with AI for theme: " + request.getTheme());

            // Prepare request for Python LLM service
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("theme", request.getTheme());
            requestBody.put("questionCount", request.getQuestionCount());
            requestBody.put("answersPerQuestion", request.getAnswersPerQuestion());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Call Python LLM service
            ResponseEntity<LLMServiceResponse> response = restTemplate.exchange(
                    llmServiceUrl + "/generate-survey",
                    HttpMethod.POST,
                    entity,
                    LLMServiceResponse.class
            );

            long processingTime = System.currentTimeMillis() - startTime;
            LLMServiceResponse llmResponse = response.getBody();

            if (llmResponse != null && llmResponse.getQuestions() != null) {
                System.out.println("✅ Successfully generated survey with AI in " + processingTime + "ms");
                
                return AIGenerateSurveyResponse.builder()
                        .questions(llmResponse.getQuestions())
                        .provider(llmResponse.getModel() != null ? llmResponse.getModel() : "Mixtral 8x7B")
                        .processingTimeMs(processingTime)
                        .build();
            } else {
                throw new RuntimeException("Invalid response from LLM service");
            }

        } catch (ResourceAccessException e) {
            long processingTime = System.currentTimeMillis() - startTime;
            System.err.println("❌ LLM service timeout: " + e.getMessage());

            return AIGenerateSurveyResponse.builder()
                    .questions(null)
                    .processingTimeMs(processingTime)
                    .errorMessage("LLM service is not available or timed out. Please ensure the Python LLM service is running on " + llmServiceUrl)
                    .build();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            long processingTime = System.currentTimeMillis() - startTime;
            System.err.println("❌ LLM service error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());

            return AIGenerateSurveyResponse.builder()
                    .questions(null)
                    .processingTimeMs(processingTime)
                    .errorMessage("LLM service error: " + e.getMessage())
                    .build();

        } catch (Exception e) {
            long processingTime = System.currentTimeMillis() - startTime;
            System.err.println("❌ Unexpected error during AI generation: " + e.getMessage());
            e.printStackTrace();

            return AIGenerateSurveyResponse.builder()
                    .questions(null)
                    .processingTimeMs(processingTime)
                    .errorMessage("Failed to generate survey: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Check if LLM service is healthy
     */
    public boolean isLLMServiceHealthy() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    llmServiceUrl + "/health",
                    String.class
            );
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get LLM service info
     */
    public Map<String, Object> getLLMServiceInfo() {
        Map<String, Object> info = new HashMap<>();
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    llmServiceUrl + "/",
                    Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                info.putAll(response.getBody());
            }
            info.put("status", "healthy");
        } catch (Exception e) {
            info.put("status", "unhealthy");
            info.put("error", e.getMessage());
        }
        info.put("url", llmServiceUrl);
        return info;
    }

    // Inner class to match Python LLM service response
    private static class LLMServiceResponse {
        private List<QuestionDTO> questions;
        private Double processing_time;
        private String model;
        private String errorMessage;

        public List<QuestionDTO> getQuestions() {
            return questions;
        }

        public void setQuestions(List<QuestionDTO> questions) {
            this.questions = questions;
        }

        public Double getProcessing_time() {
            return processing_time;
        }

        public void setProcessing_time(Double processing_time) {
            this.processing_time = processing_time;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}

