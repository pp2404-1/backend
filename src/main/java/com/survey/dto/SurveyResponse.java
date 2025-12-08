package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Survey Response DTO
 * Returned after creating a survey
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse {

    private SurveyDTO survey;
    private String message;
}

