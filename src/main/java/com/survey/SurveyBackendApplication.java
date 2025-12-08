package com.survey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Survey Backend Application
 * 
 * Main application class for the Survey Management System.
 * Provides:
 * - JWT-based authentication
 * - Survey creation and management
 * - AI-powered survey generation via LLM
 * - Survey response collection and statistics
 * 
 * @author Survey Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableConfigurationProperties
public class SurveyBackendApplication {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Survey Backend Service Starting...   ");
        System.out.println("========================================");
        
        SpringApplication.run(SurveyBackendApplication.class, args);
        
        System.out.println("\n========================================");
        System.out.println("   Survey Backend Service Started! ✓    ");
        System.out.println("   API: http://localhost:8080/api/v1    ");
        System.out.println("   Swagger: http://localhost:8080/swagger-ui.html");
        System.out.println("   H2 Console: http://localhost:8080/h2-console");
        System.out.println("========================================\n");
    }
}

