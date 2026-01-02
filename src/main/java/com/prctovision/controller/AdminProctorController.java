package com.prctovision.controller;

import com.prctovision.repository.ProctorLogRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminProctorController {

    private final ProctorLogRepository proctorLogRepository;

    public AdminProctorController(ProctorLogRepository proctorLogRepository) {
        this.proctorLogRepository = proctorLogRepository;
    }

    @GetMapping("/proctoring")
    public ResponseEntity<?> getAllProctorLogs() {
        var logs = proctorLogRepository.findAllWithStudentAndExam();

        var result = logs.stream().map(log -> {
            Map<String, Object> examMap = new HashMap<>();
            Map<String, Object> studentMap = new HashMap<>();

            // Handle nulls safely
            if (log.getExam() != null && log.getExam().getTitle() != null) {
                examMap.put("title", log.getExam().getTitle());
            } else {
                examMap.put("title", "N/A");
            }

            if (log.getStudent() != null && log.getStudent().getUsername() != null) {
                studentMap.put("username", log.getStudent().getUsername());
            } else {
                studentMap.put("username", "N/A");
            }

            Map<String, Object> map = new HashMap<>();
            map.put("id", log.getId());
            map.put("exam", examMap);
            map.put("student", studentMap);
            map.put("event", log.getEventType() != null ? log.getEventType().name() : "UNKNOWN");
            map.put("message", log.getEventMessage());
            map.put("evidence_path", log.getEvidencePath());
            map.put("timestamp", log.getEventTime());

            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
