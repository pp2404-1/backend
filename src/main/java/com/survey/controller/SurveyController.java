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

@RestController
@RequestMapping("/api/v1/surveys")
@Tag(name = "Surveys", description = "Survey management and public endpoints")
@CrossOrigin(origins = "*")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

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

    private record ErrorResponse(String message) {}
}

