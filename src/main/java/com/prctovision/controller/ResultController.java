package com.prctovision.controller;

import com.prctovision.model.Result;
import com.prctovision.repository.ResultRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class ResultController {

    private final ResultRepository resultRepo;

    public ResultController(ResultRepository resultRepo) {
        this.resultRepo = resultRepo;
    }

    // ✅ Get all results (for admin view)
    @GetMapping("/all")
    public ResponseEntity<?> getAllResults() {
        try {
            List<Result> results = resultRepo.findAll();

            List<Map<String, Object>> response = results.stream().map(r -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", r.getId());
                map.put("marks", r.getScore());
                map.put("completedAt", r.getCompletedAt());

                Map<String, Object> studentMap = new HashMap<>();
                if (r.getStudent() != null) {
                    studentMap.put("id", r.getStudent().getId());
                    studentMap.put("username", r.getStudent().getUsername());
                    studentMap.put("email", r.getStudent().getEmail());
                } else {
                    studentMap.put("username", "N/A");
                }

                Map<String, Object> examMap = new HashMap<>();
                if (r.getExam() != null) {
                    examMap.put("id", r.getExam().getId());
                    examMap.put("title", r.getExam().getTitle());
                } else {
                    examMap.put("title", "N/A");
                }

                map.put("student", studentMap);
                map.put("exam", examMap);

                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    // ✅ Get all results for a specific student (for student dashboard)
    @GetMapping("/student/{id}")
    public ResponseEntity<?> getStudentResults(@PathVariable Long id) {
        try {
            List<Result> results = resultRepo.findByStudentId(id);

            List<Map<String, Object>> response = results.stream().map(r -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", r.getId());
                map.put("marks", r.getScore());
                map.put("completedAt", r.getCompletedAt());

                Map<String, Object> examMap = new HashMap<>();
                if (r.getExam() != null) {
                    examMap.put("id", r.getExam().getId());
                    examMap.put("title", r.getExam().getTitle());
                } else {
                    examMap.put("title", "N/A");
                }

                map.put("exam", examMap);
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
