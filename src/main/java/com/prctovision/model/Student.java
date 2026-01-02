package com.prctovision.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String passwordHash;

    @Column(nullable=false)
    private String role;

    @Column(nullable=false)
    private Boolean active;

    @Column(nullable=false)
    private LocalDateTime createdAt;

    @Column(nullable=false)
    private String profilePic = "default.png";

    // Add relation to Exam
    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    // Getters and Setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getProfilePic() { return profilePic; }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }
    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }
}
