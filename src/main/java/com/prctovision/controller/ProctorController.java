package com.prctovision.controller;

import com.prctovision.model.ProctorLog;
import com.prctovision.model.Student;
import com.prctovision.model.Exam;
import com.prctovision.repository.ProctorLogRepository;
import com.prctovision.repository.StudentRepository;
import com.prctovision.repository.ExamRepository;
import com.prctovision.service.FileStorageService;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/proctor")
public class ProctorController {

    private final ProctorLogRepository proctorLogRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    private final FileStorageService fileStorageService;

    public ProctorController(ProctorLogRepository proctorLogRepository,
                             StudentRepository studentRepository,
                             ExamRepository examRepository,
                             FileStorageService fileStorageService) {
        this.proctorLogRepository = proctorLogRepository;
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/log")
    public ResponseEntity<?> logEvent(
            @RequestParam Long studentId,
            @RequestParam Long examId,
            @RequestParam ProctorLog.EventType eventType,
            @RequestParam(required = false) String eventMessage,
            @RequestParam(required = false) MultipartFile evidence
    ) throws IOException {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        ProctorLog log = new ProctorLog();
        log.setStudent(student);
        log.setExam(exam);
        log.setEventType(eventType);
        log.setEventMessage(eventMessage);
        log.setEventTime(LocalDateTime.now());

        if (evidence != null && !evidence.isEmpty()) {
            String path = fileStorageService.saveFile(evidence);
            log.setEvidencePath(path);
        }

        proctorLogRepository.save(log);

        return ResponseEntity.ok("Event logged successfully");
    }
}
