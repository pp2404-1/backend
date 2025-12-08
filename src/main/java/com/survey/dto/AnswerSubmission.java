package com.survey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Answer Submission DTO
 * Represents a single answer selection in a survey response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerSubmission {

    @NotBlank(message = "Question ID is required")
    private String questionId;

    @NotNull(message = "Selected answer index is required")
    private Integer selectedAnswerIndex;
}

