package com.survey.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI Generate Survey Request DTO
 * Request to LLM service for AI-powered survey generation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIGenerateSurveyRequest {

    @NotBlank(message = "Theme is required")
    @Size(min = 3, max = 200, message = "Theme must be between 3 and 200 characters")
    private String theme;

    @Min(value = 1, message = "Question count must be at least 1")
    @Max(value = 10, message = "Question count must be at most 10")
    @Builder.Default
    private Integer questionCount = 3;

    @Min(value = 2, message = "Answers per question must be at least 2")
    @Max(value = 5, message = "Answers per question must be at most 5")
    @Builder.Default
    private Integer answersPerQuestion = 4;
}

