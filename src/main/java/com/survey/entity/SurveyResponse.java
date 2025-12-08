package com.survey.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SurveyResponse Entity
 * Represents a user's submission/response to a survey
 */
@Entity
@Table(name = "survey_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @OneToMany(mappedBy = "surveyResponse", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ResponseAnswer> responseAnswers = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @Column(length = 100)
    private String ipAddress; // Optional: track IP for analytics

    // Helper method to add response answer
    public void addResponseAnswer(ResponseAnswer responseAnswer) {
        responseAnswers.add(responseAnswer);
        responseAnswer.setSurveyResponse(this);
    }
}

