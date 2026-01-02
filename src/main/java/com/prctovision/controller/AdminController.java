package com.prctovision.controller;

import com.prctovision.model.Admin;
import com.prctovision.model.Student;
import com.prctovision.model.Exam;
import com.prctovision.model.ExamInvite;
import com.prctovision.repository.AdminRepository;
import com.prctovision.repository.StudentRepository;
import com.prctovision.repository.ExamRepository;
import com.prctovision.repository.ExamInviteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private ExamRepository examRepo;

    @Autowired
    private ExamInviteRepository inviteRepo;

    // -------------------------
    // Existing Student/Admin Endpoints
    // -------------------------

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        return studentRepo.save(student);
    }

    @PutMapping("/students/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student updated) {
        return studentRepo.findById(id).map(student -> {
            student.setUsername(updated.getUsername());
            student.setEmail(updated.getEmail());
            student.setRole(updated.getRole());
            student.setActive(updated.getActive());
            return studentRepo.save(student);
        }).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @DeleteMapping("/students/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepo.deleteById(id);
        return "Student deleted successfully";
    }

    @GetMapping("/info/{id}")
    public Optional<Admin> getAdmin(@PathVariable Long id) {
        return adminRepo.findById(id);
    }

    // -------------------------
    // Invite Student Endpoint
    // -------------------------
    @PostMapping("/invite")
    public ExamInvite inviteStudent(@RequestParam String email, @RequestParam Long examId) {
        Exam exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        ExamInvite invite = new ExamInvite();
        invite.setExam(exam);
        invite.setEmail(email);

        // Generate random access code (8 characters)
        String code = java.util.UUID.randomUUID().toString().substring(0, 8);
        invite.setAccessCode(code);
        invite.setUsed(false);
        invite.setSentAt(java.time.LocalDateTime.now());

        inviteRepo.save(invite);
        return invite; // frontend can display access code or send via email
    }

    // -------------------------
    // Get All Invites Endpoint
    // -------------------------
    @GetMapping("/invites")
    public List<ExamInvite> getAllInvites() {
        return inviteRepo.findAll();
    }
}
