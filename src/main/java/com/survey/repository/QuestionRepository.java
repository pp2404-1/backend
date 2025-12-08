package com.survey.repository;

import com.survey.entity.Question;
import com.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Question Repository
 * Handles database operations for Question entities
 */
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    List<Question> findBySurveyOrderByQuestionOrderAsc(Survey survey);
}

