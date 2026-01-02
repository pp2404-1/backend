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

@RestController
@RequestMapping("/api/v1/llm")
@Tag(name = "LLM Service", description = "Эндпоинты управления ИИ для генерации опросов")
@CrossOrigin(origins = "*")
public class LLMController {

    @Autowired
    private LLMService llmService;

    @PostMapping("/generate-survey")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(
        summary = "Создание опроса с использованием ИИ", 
        description = "Создание вопросов и ответов опроса с использованием ИИ (Mixtral 8x7B) на основе темы"
    )
    public ResponseEntity<AIGenerateSurveyResponse> generateSurvey(
            @Valid @RequestBody AIGenerateSurveyRequest request) {
        
        AIGenerateSurveyResponse response = llmService.generateSurvey(request);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Проверка работоспособности LLM service", description = "Проверка доступности Python LLM service")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> info = llmService.getLLMServiceInfo();
        
        boolean isHealthy = "healthy".equals(info.get("status"));
        HttpStatus status = isHealthy ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        
        return ResponseEntity.status(status).body(info);
    }
}

