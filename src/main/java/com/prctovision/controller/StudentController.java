package com.prctovision.controller;

import com.prctovision.model.Student;
import com.prctovision.model.Exam;
import com.prctovision.model.Result;
import com.prctovision.model.Notification;
import com.prctovision.service.StudentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable Long id) {
        try {
            Student student = studentService.getStudentById(id);
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<?> updateStudent(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam(required = false) MultipartFile profilePic
    ) {
        try {
            Student updatedStudent = studentService.updateStudent(id, username, email, profilePic);
            return ResponseEntity.ok(updatedStudent);
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/exams")
    public List<Exam> getUpcomingExams(@PathVariable Long id) {
        return studentService.getUpcomingExams(id);
    }

    @GetMapping("/{id}/results")
    public List<Result> getPastResults(@PathVariable Long id) {
        return studentService.getPastResults(id);
    }

    @GetMapping("/{id}/notifications")
    public List<Notification> getNotifications(@PathVariable Long id) {
        return studentService.getNotifications(id);
    }

    @GetMapping("/{id}/notifications/unread")
    public List<Notification> getUnreadNotifications(@PathVariable Long id) {
        return studentService.getUnreadNotifications(id);
    }
}
