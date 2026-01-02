package com.survey.repository;

import com.survey.entity.Answer;
import com.survey.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    List<Answer> findByQuestionOrderByAnswerOrderAsc(Question question);
}

