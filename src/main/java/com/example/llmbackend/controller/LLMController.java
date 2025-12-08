package com.example.llmbackend.controller;

import com.example.llmbackend.dto.*;
import com.example.llmbackend.service.LLMService;
import com.example.llmbackend.service.RateLimitService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/llm")
@CrossOrigin(origins = "*")
public class LLMController {

    private final LLMService llmService;
    private final RateLimitService rateLimitService;

    public LLMController(LLMService llmService, RateLimitService rateLimitService) {
        this.llmService = llmService;
        this.rateLimitService = rateLimitService;
    }

    @PostMapping("/generate-survey")
    public ResponseEntity<SurveyResponse> generateSurvey(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            @RequestHeader(value = "X-Forwarded-For", required = false) String forwardedFor,
            @Valid @RequestBody SurveyRequest request) {

        String clientIp = forwardedFor != null ? forwardedFor.split(",")[0] : "unknown";
        String clientId = rateLimitService.getClientId(apiKey, clientIp);

        // Check rate limit
        if (!rateLimitService.checkRateLimit(clientId)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new SurveyResponse("Rate limit exceeded. Please try again later."));
        }

        System.out.println("🎯 Received survey generation request: " + request.getTheme() + " from " + clientId);

        SurveyResponse response = llmService.generateSurvey(request);

        if (response.getErrorMessage() != null) {
            System.err.println("⚠️ Returning error response: " + response.getErrorMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }

        System.out.println("✅ Successfully generated survey with " +
                response.getQuestions().size() + " questions");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> healthCheck() {
        boolean ollamaHealthy = llmService.isOllamaHealthy();
        boolean pythonServiceHealthy = llmService.isPythonServiceHealthy();
        boolean overallHealthy = ollamaHealthy && pythonServiceHealthy;

        HealthResponse response = new HealthResponse();
        response.setStatus(overallHealthy ? "HEALTHY" : "UNHEALTHY");
        response.setOllama(ollamaHealthy ? "RUNNING" : "STOPPED");
        response.setPythonService(pythonServiceHealthy ? "RUNNING" : "STOPPED");

        HttpStatus status = overallHealthy ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/model-info")
    public ResponseEntity<ModelInfo> getModelInfo() {
        ModelInfo info = llmService.getModelInfo();
        return ResponseEntity.ok(info);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "LLM Backend is running! 🚀");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }
}