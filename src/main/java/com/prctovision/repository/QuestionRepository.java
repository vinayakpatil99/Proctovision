package com.prctovision.repository;

import com.prctovision.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Fetch all questions by Exam entity
    List<Question> findByExam(com.prctovision.model.Exam exam);

    // Fetch all questions by exam ID
    List<Question> findByExamId(Long examId);
}
