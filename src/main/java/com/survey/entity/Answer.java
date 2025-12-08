package com.survey.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Answer Entity
 * Represents a possible answer option for a question
 */
@Entity
@Table(name = "answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String answerText;

    @Column(nullable = false)
    private Integer answerOrder; // Order in the question (0, 1, 2...)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}

