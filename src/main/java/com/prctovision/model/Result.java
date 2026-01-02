package com.prctovision.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "results")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Relationships
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    // ✅ Core result fields
    private int totalQuestions;
    private int correctAnswers;
    private double scorePercentage;

    // ✅ Optional: for compatibility with older methods
    private Double score;

    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;

    private String status; // e.g. COMPLETED, IN_PROGRESS

    private LocalDateTime completedAt;

    public Result() {}

    // ✅ Getters & Setters
    public Long getId() { return id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }

    public double getScorePercentage() { return scorePercentage; }
    public void setScorePercentage(double scorePercentage) { this.scorePercentage = scorePercentage; }

    public Double getScore() {
        // fallback: use scorePercentage if score is not explicitly set
        return score != null ? score : scorePercentage;
    }
    public void setScore(Double score) { this.score = score; }

    public Date getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Date submittedAt) { this.submittedAt = submittedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}
