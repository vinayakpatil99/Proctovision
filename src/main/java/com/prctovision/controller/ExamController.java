package com.prctovision.controller;

import com.prctovision.model.Exam;
import com.prctovision.model.Student;
import com.prctovision.model.ExamAssignment;
import com.prctovision.repository.ExamRepository;
import com.prctovision.repository.StudentRepository;
import com.prctovision.repository.ExamAssignmentRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/admin/exams")
@CrossOrigin(origins = "*")
public class ExamController {

    private final ExamRepository examRepo;
    private final StudentRepository studentRepo;
    private final ExamAssignmentRepository assignmentRepo;

    public ExamController(ExamRepository examRepo, StudentRepository studentRepo,
                          ExamAssignmentRepository assignmentRepo) {
        this.examRepo = examRepo;
        this.studentRepo = studentRepo;
        this.assignmentRepo = assignmentRepo;
    }

    // Get all exams
    @GetMapping
    public List<Exam> getAllExams() {
        return examRepo.findAll();
    }

    // Create exam
    @PostMapping
    public ResponseEntity<?> createExam(@RequestBody Exam exam) {
        try {
            if (exam.getCreatedAt() == null) {
                exam.setCreatedAt(java.time.LocalDateTime.now());
            }
            Exam saved = examRepo.save(exam);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to add exam: " + e.getMessage());
        }
    }

    // Assign exam to students
    @PostMapping("/{examId}/assign")
    public ResponseEntity<?> assignExam(@PathVariable Long examId, @RequestBody List<Long> studentIds) {
        Exam exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        for (Long studentId : studentIds) {
            Student student = studentRepo.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found: " + studentId));
            ExamAssignment assignment = new ExamAssignment();
            assignment.setExam(exam);
            assignment.setStudent(student);
            assignmentRepo.save(assignment);
        }
        return ResponseEntity.ok("Exam assigned successfully");
    }
}
