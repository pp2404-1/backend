package com.survey.repository;

import com.survey.entity.Survey;
import com.survey.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    
    Optional<Survey> findBySurveyId(String surveyId);
    
    List<Survey> findByCreator(User creator);
    
    List<Survey> findByCreatorOrderByCreatedAtDesc(User creator);
    
    boolean existsBySurveyId(String surveyId);
    
    @Query("SELECT COUNT(sr) FROM SurveyResponse sr WHERE sr.survey.surveyId = :surveyId")
    Long countResponsesBySurveyId(String surveyId);
}

