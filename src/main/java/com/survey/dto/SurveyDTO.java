package com.survey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Survey DTO
 * Full survey information including questions and answers
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyDTO {

    private String id;
    private String title;
    private List<QuestionDTO> questions;
    private String createdAt;
    private String creatorId;
}

