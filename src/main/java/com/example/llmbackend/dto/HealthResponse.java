package com.example.llmbackend.dto;

public class HealthResponse {
    private String status;
    private String ollama;
    private String pythonService;
    private Long timestamp;

    public HealthResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public HealthResponse(String status) {
        this();
        this.status = status;
    }

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOllama() { return ollama; }
    public void setOllama(String ollama) { this.ollama = ollama; }

    public String getPythonService() { return pythonService; }
    public void setPythonService(String pythonService) { this.pythonService = pythonService; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}