package com.survey.controller;

import com.survey.dto.*;
import com.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Survey Controller
 * Handles all survey-related endpoints
 */
@RestController
@RequestMapping("/api/v1/surveys")
@Tag(name = "Surveys", description = "Survey management and public endpoints")
@CrossOrigin(origins = "*")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    /**
     * Get all surveys created by current user
     * GET /api/v1/surveys/my
     * Protected endpoint - requires authentication
     */
    @GetMapping("/my")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Get my surveys", description = "Get all surveys created by authenticated user")
    public ResponseEntity<?> getMySurveys() {
        try {
            List<SurveyDTO> surveys = surveyService.getMySurveys();
            return ResponseEntity.ok(surveys);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Create a new survey
     * POST /api/v1/surveys/create
     * Protected endpoint - requires authentication
     */
    @PostMapping("/create")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Create survey", description = "Create a new survey")
    public ResponseEntity<?> createSurvey(@Valid @RequestBody CreateSurveyRequest request) {
        try {
            SurveyResponse response = surveyService.createSurvey(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Get survey by ID
     * GET /api/v1/surveys/{surveyId}
     * Public endpoint - no authentication required
     */
    @GetMapping("/{surveyId}")
    @Operation(summary = "Get survey", description = "Get survey details by ID (public access)")
    public ResponseEntity<?> getSurveyById(@PathVariable String surveyId) {
        try {
            SurveyDTO survey = surveyService.getSurveyById(surveyId);
            return ResponseEntity.ok(survey);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Delete survey
     * DELETE /api/v1/surveys/{surveyId}
     * Protected endpoint - requires authentication
     */
    @DeleteMapping("/{surveyId}")
    @SecurityRequirement(name = "bearer-auth")
    @Operation(summary = "Delete survey", description = "Delete survey by ID")
    public ResponseEntity<?> deleteSurvey(@PathVariable String surveyId) {
        try {
            surveyService.deleteSurvey(surveyId);
            return ResponseEntity.ok(new MessageResponse("Survey deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Submit survey answers
     * POST /api/v1/surveys/submit
     * Public endpoint - no authentication required
     */
    @PostMapping("/submit")
    @Operation(summary = "Submit survey", description = "Submit answers to a survey (public access)")
    public ResponseEntity<?> submitSurveyAnswers(@Valid @RequestBody SurveyAnswerSubmission submission) {
        try {
            surveyService.submitSurveyAnswers(submission);
            return ResponseEntity.ok(new MessageResponse("Answers submitted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Get survey results
     * GET /api/v1/surveys/{surveyId}/results
     * Public endpoint - no authentication required
     */
    @GetMapping("/{surveyId}/results")
    @Operation(summary = "Get survey results", description = "Get statistical results for a survey (public access)")
    public ResponseEntity<?> getSurveyResults(@PathVariable String surveyId) {
        try {
            SurveyResultsDTO results = surveyService.getSurveyResults(surveyId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    // Error response helper class
    private record ErrorResponse(String message) {}
}

