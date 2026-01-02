package com.prctovision.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="exam_invites")
public class ExamInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Exam exam;

    private String email;  // Candidate email

    private String accessCode; // Unique code for login

    private Boolean used = false;

    private LocalDateTime sentAt;

    // New fields for temporary login
    private String tempUsername;
    private String tempPasswordHash;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    public enum Status {
        PENDING, USED, EXPIRED
    }

    // Getters and Setters
    public Long getId() { return id; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAccessCode() { return accessCode; }
    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }

    public Boolean getUsed() { return used; }
    public void setUsed(Boolean used) { this.used = used; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public String getTempUsername() { return tempUsername; }
    public void setTempUsername(String tempUsername) { this.tempUsername = tempUsername; }

    public String getTempPasswordHash() { return tempPasswordHash; }
    public void setTempPasswordHash(String tempPasswordHash) { this.tempPasswordHash = tempPasswordHash; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
