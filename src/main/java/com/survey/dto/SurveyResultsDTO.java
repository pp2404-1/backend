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
public class SurveyResultsDTO {

    private String surveyId;
    private Long totalResponses;
    private List<QuestionResultDTO> questions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QuestionResultDTO {
        private String questionId;
        private String question;
        private List<AnswerResultDTO> answers;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnswerResultDTO {
        private String text;
        private Long count;
        private Double percentage;
    }
}

