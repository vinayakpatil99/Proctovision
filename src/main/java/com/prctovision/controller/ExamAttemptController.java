package com.prctovision.controller;

import com.prctovision.dto.QuestionDTO;
import com.prctovision.model.*;
import com.prctovision.repository.*;
import com.prctovision.service.ResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "*")
public class ExamAttemptController {

    private final ExamRepository examRepo;
    private final ExamAssignmentRepository assignmentRepo;
    private final QuestionRepository questionRepo;
    private final StudentAnswerRepository answerRepo;
    private final StudentRepository studentRepo;
    private final ResultRepository resultRepository;
    private final ProctorLogRepository proctorLogRepository;
    private final ResultService resultService;

    public ExamAttemptController(
            ExamRepository examRepo,
            ExamAssignmentRepository assignmentRepo,
            QuestionRepository questionRepo,
            StudentAnswerRepository answerRepo,
            StudentRepository studentRepo,
            ResultRepository resultRepository,
            ProctorLogRepository proctorLogRepository,
            ResultService resultService) {
        this.examRepo = examRepo;
        this.assignmentRepo = assignmentRepo;
        this.questionRepo = questionRepo;
        this.answerRepo = answerRepo;
        this.studentRepo = studentRepo;
        this.resultRepository = resultRepository;
        this.proctorLogRepository = proctorLogRepository;
        this.resultService = resultService;
    }

    // ✅ NEW — Fetch all exams for a student with completion status
    @GetMapping("/listWithCompletion")
    public ResponseEntity<?> getAllExamsWithCompletion(@RequestParam Long studentId) {
        List<ExamAssignment> assignments = assignmentRepo.findByStudentId(studentId);

        List<Result> results = resultRepository.findByStudentId(studentId);
        Set<Long> completedExamIds = results.stream()
                .map(r -> r.getExam().getId())
                .collect(Collectors.toSet());

        List<Map<String, Object>> examsWithStatus = assignments.stream()
                .map(a -> {
                    Exam e = a.getExam();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", e.getId());
                    map.put("title", e.getTitle());
                    map.put("examDate", e.getExamDate());
                    map.put("durationMinutes", e.getDurationMinutes());
                    map.put("status", a.getStatus());
                    map.put("completed", completedExamIds.contains(e.getId())); // ✅ key field
                    return map;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(examsWithStatus);
    }

    // ✅ Fetch active exams assigned to a student
    @GetMapping("/assignedList")
    public ResponseEntity<?> getAssignedExams(@RequestParam Long studentId) {
        List<ExamAssignment> assignments = assignmentRepo.findByStudentId(studentId);

        List<Exam> exams = assignments.stream()
                .filter(a ->
                        ("PENDING".equalsIgnoreCase(a.getStatus()) || "IN_PROGRESS".equalsIgnoreCase(a.getStatus())) &&
                                a.getExam() != null &&
                                a.getExam().getExamDate() != null &&
                                a.getExam().getExamDate().isEqual(LocalDate.now())
                )
                .map(ExamAssignment::getExam)
                .collect(Collectors.toList());

        return ResponseEntity.ok(exams);
    }

    // ✅ Start or Resume Exam
    @PostMapping("/{examId}/startAndFetch")
    public ResponseEntity<?> startExamAndFetchQuestions(@PathVariable Long examId,
                                                        @RequestParam Long studentId) {
        Optional<ExamAssignment> assignmentOpt = assignmentRepo.findByExamIdAndStudentId(examId, studentId);
        if (assignmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Student not assigned to this exam"));
        }

        ExamAssignment assignment = assignmentOpt.get();
        Exam exam = assignment.getExam();
        LocalDateTime now = LocalDateTime.now();

        if (exam.getExamDate() == null || !exam.getExamDate().isEqual(LocalDate.now())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Exam not scheduled for today"));
        }

        long remainingMinutes;

        if ("IN_PROGRESS".equals(assignment.getStatus()) && assignment.getStartedAt() != null) {
            long elapsedMinutes = Duration.between(assignment.getStartedAt(), now).toMinutes();
            remainingMinutes = Math.max(0, exam.getDurationMinutes() - elapsedMinutes);
        } else {
            assignment.setStatus("IN_PROGRESS");
            assignment.setStartedAt(now);
            assignmentRepo.save(assignment);
            remainingMinutes = exam.getDurationMinutes();
        }

        if (remainingMinutes <= 0) {
            assignment.setStatus("COMPLETED");
            assignmentRepo.save(assignment);
            return ResponseEntity.badRequest().body(Map.of("error", "Exam time has expired."));
        }

        List<Question> questions = questionRepo.findByExamId(examId);
        List<QuestionDTO> questionDTOs = questions.stream()
                .map(q -> new QuestionDTO(
                        q.getId(),
                        q.getExam().getId(),
                        q.getExam().getTitle(),
                        q.getTitle(),
                        q.getType() != null ? q.getType().name() : "MCQ",
                        q.getOptions()
                )).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("examInfo", exam);
        response.put("questionsList", questionDTOs);
        response.put("remainingTime", remainingMinutes);
        response.put("status", assignment.getStatus());
        response.put("startedAt", assignment.getStartedAt());

        return ResponseEntity.ok(response);
    }

    // ✅ Save Student Answer
    @PostMapping("/{examId}/answer")
    public ResponseEntity<?> saveAnswer(@PathVariable Long examId,
                                        @RequestParam Long studentId,
                                        @RequestParam Long questionId,
                                        @RequestParam(required = false) String answerText) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Exam exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        StudentAnswer answer = answerRepo.findByStudentIdAndExamIdAndQuestionId(studentId, examId, questionId)
                .orElse(new StudentAnswer());

        answer.setStudent(student);
        answer.setExam(exam);
        answer.setQuestion(question);
        answer.setAnswerText(answerText);
        answer.setSubmittedAt(LocalDateTime.now());
        answerRepo.save(answer);

        return ResponseEntity.ok(Map.of("message", "Answer saved successfully"));
    }

    // ✅ Submit Exam + Auto Result + Proctor Log
    @PostMapping("/{examId}/submit")
    public ResponseEntity<?> submitExam(@PathVariable Long examId,
                                        @RequestParam Long studentId) {
        Optional<ExamAssignment> assignmentOpt = assignmentRepo.findByExamIdAndStudentId(examId, studentId);
        if (assignmentOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Assignment not found"));
        }

        ExamAssignment assignment = assignmentOpt.get();
        assignment.setStatus("COMPLETED");
        assignment.setCompletedAt(LocalDateTime.now());
        assignmentRepo.save(assignment);

        Student student = studentRepo.findById(studentId).orElseThrow();
        Exam exam = examRepo.findById(examId).orElseThrow();
        List<StudentAnswer> answers = answerRepo.findByStudentAndExam(student, exam);

        int totalQuestions = answers.size();
        int correct = 0;

        for (StudentAnswer ans : answers) {
            if (ans.getAnswerText() != null &&
                    ans.getQuestion().getAnswer() != null &&
                    ans.getAnswerText().trim().equalsIgnoreCase(ans.getQuestion().getAnswer().trim())) {
                correct++;
            }
        }

        double percentage = totalQuestions > 0 ? (correct * 100.0 / totalQuestions) : 0;

        Result result = resultRepository.findByStudentAndExam(student, exam)
                .orElse(new Result());
        result.setStudent(student);
        result.setExam(exam);
        result.setTotalQuestions(totalQuestions);
        result.setCorrectAnswers(correct);
        result.setScorePercentage(percentage);
        result.setSubmittedAt(new Date());
        result.setStatus("COMPLETED");
        resultService.saveResult(result);

        ProctorLog log = new ProctorLog();
        log.setStudent(student);
        log.setExam(exam);
        log.setEventType(ProctorLog.EventType.EXAM_SUBMITTED);
        log.setEventMessage("Exam submitted successfully by student.");
        log.setEventTime(LocalDateTime.now());
        proctorLogRepository.save(log);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Exam submitted successfully");
        response.put("correctAnswers", correct);
        response.put("totalQuestions", totalQuestions);
        response.put("percentage", percentage);

        return ResponseEntity.ok(response);
    }
}
