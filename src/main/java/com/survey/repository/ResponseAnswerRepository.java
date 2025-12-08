package com.survey.repository;

import com.survey.entity.Answer;
import com.survey.entity.Question;
import com.survey.entity.ResponseAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ResponseAnswer Repository
 * Handles database operations for ResponseAnswer entities
 */
@Repository
public interface ResponseAnswerRepository extends JpaRepository<ResponseAnswer, Long> {
    
    Long countByAnswer(Answer answer);
    
    Long countByQuestion(Question question);
    
    @Query("SELECT ra.answer, COUNT(ra) FROM ResponseAnswer ra WHERE ra.question = :question GROUP BY ra.answer")
    List<Object[]> countAnswersByQuestion(@Param("question") Question question);
}

