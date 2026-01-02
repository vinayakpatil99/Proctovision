package com.prctovision.repository;

import com.prctovision.model.StudentAnswer;
import com.prctovision.model.Student;
import com.prctovision.model.Exam;
import com.prctovision.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {

    // ✅ Existing: all answers of a student for one exam
    List<StudentAnswer> findByStudentAndExam(Student student, Exam exam);

    // ✅ New: find by studentId, examId, questionId (used in ExamAttemptController)
    Optional<StudentAnswer> findByStudentIdAndExamIdAndQuestionId(Long studentId, Long examId, Long questionId);
}
