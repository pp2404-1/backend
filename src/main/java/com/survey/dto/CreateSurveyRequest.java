package com.survey.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Create Survey Request DTO
 * Used to create a new survey
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSurveyRequest {

    @NotBlank(message = "Survey title is required")
    @Size(min = 1, max = 500, message = "Title must be between 1 and 500 characters")
    private String title;

    @NotEmpty(message = "At least one question is required")
    @Size(min = 1, max = 50, message = "Survey must have between 1 and 50 questions")
    @Valid
    private List<QuestionDTO> questions;
}

