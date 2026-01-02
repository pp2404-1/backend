package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIGenerateSurveyResponse {

    private List<QuestionDTO> questions;
    private String provider;
    private Long processingTimeMs;
    private String errorMessage;
}

