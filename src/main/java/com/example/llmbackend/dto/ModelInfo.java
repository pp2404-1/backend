package com.example.llmbackend.dto;

public class ModelInfo {
    private String ollamaVersion;
    private String model;
    private String status;
    private Boolean gpuAvailable;

    public ModelInfo() {}

    public ModelInfo(String ollamaVersion, String model, String status, Boolean gpuAvailable) {
        this.ollamaVersion = ollamaVersion;
        this.model = model;
        this.status = status;
        this.gpuAvailable = gpuAvailable;
    }

    // Getters and Setters
    public String getOllamaVersion() { return ollamaVersion; }
    public void setOllamaVersion(String ollamaVersion) { this.ollamaVersion = ollamaVersion; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Boolean getGpuAvailable() { return gpuAvailable; }
    public void setGpuAvailable(Boolean gpuAvailable) { this.gpuAvailable = gpuAvailable; }
}