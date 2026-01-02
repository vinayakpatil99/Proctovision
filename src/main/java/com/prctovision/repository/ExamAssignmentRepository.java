package com.prctovision.repository;

import com.prctovision.model.ExamAssignment;
import com.prctovision.model.Student;
import com.prctovision.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAssignmentRepository extends JpaRepository<ExamAssignment, Long> {

    // Find all assignments for a given student entity
    List<ExamAssignment> findByStudent(Student student);

    // Find all assignments for a given exam entity
    List<ExamAssignment> findByExam(Exam exam);

    // Find a specific assignment by Exam and Student entity
    Optional<ExamAssignment> findByExamAndStudent(Exam exam, Student student);

    // Find upcoming exams for a student by studentId
    @Query("SELECT ea.exam FROM ExamAssignment ea WHERE ea.student.id = :studentId AND ea.exam.examDate >= CURRENT_DATE")
    List<Exam> findUpcomingExamsByStudentId(Long studentId);

    // Find a specific assignment by examId and studentId
    Optional<ExamAssignment> findByExamIdAndStudentId(Long examId, Long studentId);

    // Find all assignments for a student using studentId
    @Query("SELECT ea FROM ExamAssignment ea WHERE ea.student.id = :studentId")
    List<ExamAssignment> findByStudentId(Long studentId);

    // Find all assignments by examId and studentId (list version)
    @Query("SELECT ea FROM ExamAssignment ea WHERE ea.exam.id = :examId AND ea.student.id = :studentId")
    List<ExamAssignment> findByExamIdAndStudentIdList(Long examId, Long studentId);

    // ✅ Find ongoing (in-progress) exams for a student
    @Query("SELECT ea FROM ExamAssignment ea WHERE ea.student.id = :studentId AND ea.status = 'IN_PROGRESS'")
    List<ExamAssignment> findInProgressExamsByStudentId(Long studentId);
}
