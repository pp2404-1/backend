package com.example.llmbackend.dto;

import java.util.List;

public class SurveyResponse {
    private List<Question> questions;
    private String errorMessage;
    private String provider;
    private Long processingTimeMs;

    public SurveyResponse() {}

    public SurveyResponse(List<Question> questions) {
        this.questions = questions;
    }

    public SurveyResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // Getters and Setters
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public Long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(Long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
}