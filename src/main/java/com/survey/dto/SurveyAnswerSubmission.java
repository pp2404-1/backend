package com.survey.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAnswerSubmission {

    @NotBlank(message = "Survey ID is required")
    private String surveyId;

    @NotEmpty(message = "At least one answer is required")
    @Valid
    private List<AnswerSubmission> answers;
}

