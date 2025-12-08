package com.example.llmbackend.dto;

import java.util.List;

public class Question {
    private String question;
    private List<String> answers;

    public Question() {}

    public Question(String question, List<String> answers) {
        this.question = question;
        this.answers = answers;
    }

    // Getters and Setters
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public List<String> getAnswers() { return answers; }
    public void setAnswers(List<String> answers) { this.answers = answers; }
}