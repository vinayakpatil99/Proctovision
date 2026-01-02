package com.prctovision.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_answers")
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Relations
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // ✅ Answer text for MCQ / descriptive
    @Column(columnDefinition = "TEXT")
    private String answerText;

    // ✅ Optional field for coding answers
    @Column(columnDefinition = "TEXT")
    private String codeSubmission;

    // ✅ Correct answer (optional for evaluation)
    @Column(columnDefinition = "TEXT")
    private String correctAnswer;

    // ✅ Timestamp
    private LocalDateTime submittedAt;

    // ✅ Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }

    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }

    public String getCodeSubmission() { return codeSubmission; }
    public void setCodeSubmission(String codeSubmission) { this.codeSubmission = codeSubmission; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
