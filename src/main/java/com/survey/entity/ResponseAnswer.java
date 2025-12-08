package com.survey.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * ResponseAnswer Entity
 * Links a survey response to a specific answer chosen for a question
 */
@Entity
@Table(name = "response_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_response_id", nullable = false)
    private SurveyResponse surveyResponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @Column(nullable = false)
    private Integer selectedAnswerIndex; // The index that was selected (0, 1, 2...)
}

