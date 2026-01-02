package com.prctovision.repository;

import com.prctovision.model.Result;
import com.prctovision.model.Student;
import com.prctovision.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    // ✅ Get all results for a specific student by ID
    List<Result> findByStudentId(Long studentId);

    // ✅ Get all results for a specific exam by ID
    List<Result> findByExamId(Long examId);

    // ✅ Get results by Student entity
    List<Result> findByStudent(Student student);

    // ✅ Get results by Exam entity
    List<Result> findByExam(Exam exam);

    // ✅ Get result for a specific student-exam pair (unique record)
    Optional<Result> findByStudentAndExam(Student student, Exam exam);
}
