package com.survey.service;

import com.survey.dto.AnswerSubmission;
import com.survey.dto.CreateSurveyRequest;
import com.survey.dto.QuestionDTO;
import com.survey.dto.SurveyAnswerSubmission;
import com.survey.dto.SurveyDTO;
import com.survey.dto.SurveyResponse;
import com.survey.dto.SurveyResultsDTO;
import com.survey.entity.Answer;
import com.survey.entity.Question;
import com.survey.entity.ResponseAnswer;
import com.survey.entity.Survey;
import com.survey.entity.User;
import com.survey.repository.AnswerRepository;
import com.survey.repository.QuestionRepository;
import com.survey.repository.ResponseAnswerRepository;
import com.survey.repository.SurveyRepository;
import com.survey.repository.SurveyResponseRepository;
import com.survey.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Survey Service
 * Handles all survey-related business logic
 */
@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private SurveyResponseRepository surveyResponseRepository;

    @Autowired
    private ResponseAnswerRepository responseAnswerRepository;

    /**
     * Get all surveys created by the current user
     */
    public List<SurveyDTO> getMySurveys() {
        User currentUser = getCurrentUser();
        List<Survey> surveys = surveyRepository.findByCreatorOrderByCreatedAtDesc(currentUser);
        
        return surveys.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create a new survey
     */
    @Transactional
    public SurveyResponse createSurvey(CreateSurveyRequest request) {
        User currentUser = getCurrentUser();

        // Generate unique survey ID
        String surveyId = generateUniqueSurveyId();

        // Create survey entity
        Survey survey = Survey.builder()
                .surveyId(surveyId)
                .title(request.getTitle())
                .creator(currentUser)
                .active(true)
                .build();

        // Add questions and answers
        for (int i = 0; i < request.getQuestions().size(); i++) {
            QuestionDTO questionDTO = request.getQuestions().get(i);
            
            Question question = Question.builder()
                    .questionId("q" + (i + 1))
                    .questionText(questionDTO.getQuestion())
                    .questionOrder(i + 1)
                    .build();

            // Add answers to question
            for (int j = 0; j < questionDTO.getAnswers().size(); j++) {
                Answer answer = Answer.builder()
                        .answerText(questionDTO.getAnswers().get(j))
                        .answerOrder(j)
                        .build();
                question.addAnswer(answer);
            }

            survey.addQuestion(question);
        }

        // Save survey
        survey = surveyRepository.save(survey);

        // Build response
        SurveyDTO surveyDTO = convertToDTO(survey);
        
        return SurveyResponse.builder()
                .survey(surveyDTO)
                .message("Survey created successfully")
                .build();
    }

    /**
     * Get survey by ID (public access)
     */
    public SurveyDTO getSurveyById(String surveyId) {
        Survey survey = surveyRepository.findBySurveyId(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        return convertToDTO(survey);
    }

    /**
     * Delete survey by ID
     */
    @Transactional
    public void deleteSurvey(String surveyId) {
        User currentUser = getCurrentUser();
        
        Survey survey = surveyRepository.findBySurveyId(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        // Check if user is the creator
        if (!survey.getCreator().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this survey");
        }

        surveyRepository.delete(survey);
    }

    /**
     * Submit survey answers
     */
    @Transactional
    public void submitSurveyAnswers(SurveyAnswerSubmission submission) {
        Survey survey = surveyRepository.findBySurveyId(submission.getSurveyId())
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        // Create survey response (entity)
        com.survey.entity.SurveyResponse surveyResponse = com.survey.entity.SurveyResponse.builder()
                .survey(survey)
                .build();

        // Add response answers
        for (AnswerSubmission answerSubmission : submission.getAnswers()) {
            // Find question
            Question question = survey.getQuestions().stream()
                    .filter(q -> q.getQuestionId().equals(answerSubmission.getQuestionId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Question not found: " + answerSubmission.getQuestionId()));

            // Find answer
            int answerIndex = answerSubmission.getSelectedAnswerIndex();
            if (answerIndex < 0 || answerIndex >= question.getAnswers().size()) {
                throw new RuntimeException("Invalid answer index for question: " + answerSubmission.getQuestionId());
            }

            Answer answer = question.getAnswers().get(answerIndex);

            // Create response answer
            ResponseAnswer responseAnswer = ResponseAnswer.builder()
                    .question(question)
                    .answer(answer)
                    .selectedAnswerIndex(answerIndex)
                    .build();

            surveyResponse.addResponseAnswer(responseAnswer);
        }

        // Save survey response
        surveyResponseRepository.save(surveyResponse);
    }

    /**
     * Get survey results/statistics
     */
    public SurveyResultsDTO getSurveyResults(String surveyId) {
        Survey survey = surveyRepository.findBySurveyId(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));

        Long totalResponses = surveyResponseRepository.countBySurvey(survey);

        List<SurveyResultsDTO.QuestionResultDTO> questionResults = new ArrayList<>();

        for (Question question : survey.getQuestions()) {
            List<SurveyResultsDTO.AnswerResultDTO> answerResults = new ArrayList<>();

            for (Answer answer : question.getAnswers()) {
                Long count = responseAnswerRepository.countByAnswer(answer);
                Double percentage = totalResponses > 0 
                        ? (count * 100.0) / totalResponses 
                        : 0.0;

                answerResults.add(SurveyResultsDTO.AnswerResultDTO.builder()
                        .text(answer.getAnswerText())
                        .count(count)
                        .percentage(Math.round(percentage * 10.0) / 10.0) // Round to 1 decimal
                        .build());
            }

            questionResults.add(SurveyResultsDTO.QuestionResultDTO.builder()
                    .questionId(question.getQuestionId())
                    .question(question.getQuestionText())
                    .answers(answerResults)
                    .build());
        }

        return SurveyResultsDTO.builder()
                .surveyId(surveyId)
                .totalResponses(totalResponses)
                .questions(questionResults)
                .build();
    }

    /**
     * Convert Survey entity to DTO
     */
    private SurveyDTO convertToDTO(Survey survey) {
        List<QuestionDTO> questionDTOs = survey.getQuestions().stream()
                .sorted(Comparator.comparing(Question::getQuestionOrder))
                .map(question -> {
                    List<String> answers = question.getAnswers().stream()
                            .sorted(Comparator.comparing(Answer::getAnswerOrder))
                            .map(Answer::getAnswerText)
                            .collect(Collectors.toList());

                    return QuestionDTO.builder()
                            .id(question.getQuestionId())
                            .question(question.getQuestionText())
                            .answers(answers)
                            .build();
                })
                .collect(Collectors.toList());

        return SurveyDTO.builder()
                .id(survey.getSurveyId())
                .title(survey.getTitle())
                .questions(questionDTOs)
                .createdAt(survey.getCreatedAt().toString())
                .creatorId("user-" + survey.getCreator().getId())
                .build();
    }

    /**
     * Generate unique survey ID
     */
    private String generateUniqueSurveyId() {
        String surveyId;
        int counter = 1;
        
        do {
            surveyId = String.format("SURVEY-%03d", counter);
            counter++;
        } while (surveyRepository.existsBySurveyId(surveyId));

        return surveyId;
    }

    /**
     * Get current authenticated user
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

