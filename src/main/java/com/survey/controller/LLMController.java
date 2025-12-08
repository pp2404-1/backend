package com.survey.controller;

import com.survey.dto.AIGenerateSurveyRequest;
import com.survey.dto.AIGenerateSurveyResponse;
import com.survey.service.LLMService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * LLM Controller
 * Handles AI-powered survey generation endpoints
 */
@RestController
@RequestMapping("/api/v1/llm")
@Tag(name = "LLM Service", description = "AI-powered survey generation endpoints")
@CrossOrigin(origins = "*")
public class LLMController {

    @Autowired
    private LLMService llmService;

    /**
     * Generate survey using AI
     * POST /api/v1/llm/generate-survey
     * Protected endpoint - requires authentication
     */
    @PostMapping("/generate-survey")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(
        summary = "Generate survey with AI", 
        description = "Generate survey questions and answers using AI (Mixtral 8x7B) based on a theme"
    )
    public ResponseEntity<AIGenerateSurveyResponse> generateSurvey(
            @Valid @RequestBody AIGenerateSurveyRequest request) {
        
        AIGenerateSurveyResponse response = llmService.generateSurvey(request);
        
        // Return response even if there's an error (errorMessage will be set)
        return ResponseEntity.ok(response);
    }

    /**
     * Check LLM service health
     * GET /api/v1/llm/health
     * Public endpoint
     */
    @GetMapping("/health")
    @Operation(summary = "Check LLM service health", description = "Check if Python LLM service is available")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> info = llmService.getLLMServiceInfo();
        
        boolean isHealthy = "healthy".equals(info.get("status"));
        HttpStatus status = isHealthy ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        
        return ResponseEntity.status(status).body(info);
    }
}

