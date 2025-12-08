package com.survey.repository;

import com.survey.entity.Survey;
import com.survey.entity.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SurveyResponse Repository
 * Handles database operations for SurveyResponse entities
 */
@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    
    List<SurveyResponse> findBySurvey(Survey survey);
    
    Long countBySurvey(Survey survey);
}

