package com.prctovision.controller;

import com.prctovision.service.InterviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/interview")
public class MockInterviewController {

    private final InterviewService interviewService;

    public MockInterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping("/start")
    public ResponseEntity<?> start(@RequestBody Map<String, String> body) {
        String role = body.getOrDefault("role", "Backend Developer");
        String experience = body.getOrDefault("experience", "Fresher");

        // Call service
        Map<String, String> result = interviewService.startInterview(role, experience);

        // ✅ Convert to mutable map
        Map<String, String> mutableResult = new HashMap<>(result);

        // Add timestamp
        mutableResult.put("startedAt", Instant.now().toString());

        return ResponseEntity.ok(mutableResult);
    }

    @PostMapping("/answer")
    public ResponseEntity<?> answer(@RequestBody Map<String, String> body) {
        String sessionId = body.get("sessionId");
        String answer = body.getOrDefault("answer", "");

        if (sessionId == null || sessionId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "sessionId required"));
        }

        String response = interviewService.submitAnswer(sessionId, answer);
        return ResponseEntity.ok(Map.of("response", response));
    }
}
