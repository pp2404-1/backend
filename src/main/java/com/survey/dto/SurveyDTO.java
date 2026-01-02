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
public class SurveyDTO {

    private String id;
    private String title;
    private List<QuestionDTO> questions;
    private String createdAt;
    private String creatorId;
}

