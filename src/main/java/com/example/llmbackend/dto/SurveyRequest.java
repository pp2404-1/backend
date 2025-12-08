package com.example.llmbackend.dto;

import jakarta.validation.constraints.*;

public class SurveyRequest {

    @NotBlank(message = "Тема опроса не может быть пустой")
    @Size(max = 100, message = "Тема не должна превышать 100 символов")
    private String theme;

    @NotNull(message = "Количество вопросов обязательно")
    @Min(value = 1, message = "Количество вопросов должно быть не менее 1")
    @Max(value = 10, message = "Количество вопросов не должно превышать 10")
    private Integer questionCount;

    @NotNull(message = "Количество ответов обязательно")
    @Min(value = 2, message = "Количество ответов должно быть не менее 2")
    @Max(value = 5, message = "Количество ответов не должно превышать 5")
    private Integer answersPerQuestion;

    public SurveyRequest() {}

    public SurveyRequest(String theme, Integer questionCount, Integer answersPerQuestion) {
        this.theme = theme;
        this.questionCount = questionCount;
        this.answersPerQuestion = answersPerQuestion;
    }

    // Getters and Setters
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public Integer getQuestionCount() { return questionCount; }
    public void setQuestionCount(Integer questionCount) { this.questionCount = questionCount; }

    public Integer getAnswersPerQuestion() { return answersPerQuestion; }
    public void setAnswersPerQuestion(Integer answersPerQuestion) { this.answersPerQuestion = answersPerQuestion; }
}