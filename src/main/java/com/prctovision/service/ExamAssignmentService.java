package com.prctovision.service;

import com.prctovision.model.ExamAssignment;
import com.prctovision.model.Student;
import com.prctovision.model.Exam;
import com.prctovision.repository.ExamAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExamAssignmentService {

    @Autowired
    private ExamAssignmentRepository assignmentRepository;

    public List<ExamAssignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Optional<ExamAssignment> getAssignmentById(Long id) {
        return assignmentRepository.findById(id);
    }

    public List<ExamAssignment> getAssignmentsByStudent(Student student) {
        return assignmentRepository.findByStudent(student);
    }

    public List<ExamAssignment> getAssignmentsByExam(Exam exam) {
        return assignmentRepository.findByExam(exam);
    }

    public Optional<ExamAssignment> getAssignmentByExamAndStudent(Exam exam, Student student) {
        return assignmentRepository.findByExamAndStudent(exam, student);
    }

    public ExamAssignment saveAssignment(ExamAssignment assignment) {
        return assignmentRepository.save(assignment);
    }

    public void deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }

    public List<ExamAssignment> getAssignmentsByStudentId(Long studentId) {
        return assignmentRepository.findByStudentId(studentId);
    }

    public Optional<ExamAssignment> getAssignmentByExamIdAndStudentId(Long examId, Long studentId) {
        return assignmentRepository.findByExamIdAndStudentId(examId, studentId);
    }

    // ✅ Start or Resume Exam with Restart Handling
    public ExamAssignment startOrResumeExam(Long examId, Long studentId, Exam exam) {
        Optional<ExamAssignment> assignmentOpt = assignmentRepository.findByExamIdAndStudentId(examId, studentId);

        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Student not assigned to this exam");
        }

        ExamAssignment assignment = assignmentOpt.get();

        // ✅ Validate date
        if (!exam.getExamDate().isEqual(LocalDate.now())) {
            throw new RuntimeException("Exam not scheduled for today");
        }

        LocalDateTime now = LocalDateTime.now();

        // ✅ Allow restarting only for same-day testing/demo
        if ("COMPLETED".equals(assignment.getStatus())) {
            assignment.setStatus("PENDING");
            assignment.setStartedAt(null);
            assignment.setCompletedAt(null);
            assignmentRepository.save(assignment);
        }

        long remainingMinutes;

        // ✅ Resume if already in progress
        if ("IN_PROGRESS".equals(assignment.getStatus()) && assignment.getStartedAt() != null) {
            long elapsed = Duration.between(assignment.getStartedAt(), now).toMinutes();
            remainingMinutes = Math.max(0, exam.getDurationMinutes() - elapsed);
        } else {
            assignment.setStatus("IN_PROGRESS");
            assignment.setStartedAt(now);
            assignmentRepository.save(assignment);
            remainingMinutes = exam.getDurationMinutes();
        }

        // ✅ Handle expired exam
        if (remainingMinutes <= 0) {
            assignment.setStatus("COMPLETED");
            assignmentRepository.save(assignment);
            throw new RuntimeException("Exam time has expired");
        }

        return assignment;
    }

    public ExamAssignment completeExam(Long examId, Long studentId) {
        Optional<ExamAssignment> assignmentOpt = assignmentRepository.findByExamIdAndStudentId(examId, studentId);

        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Exam assignment not found");
        }

        ExamAssignment assignment = assignmentOpt.get();
        assignment.setStatus("COMPLETED");
        assignment.setCompletedAt(LocalDateTime.now());
        return assignmentRepository.save(assignment);
    }
}
