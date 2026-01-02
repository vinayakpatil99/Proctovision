package com.prctovision.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "proctor_logs")
public class ProctorLog {

    public enum EventType {
        FACE_NOT_VISIBLE,
        MULTIPLE_FACES,
        TAB_SWITCH,        // ✅ Added back to support old DB values
        SWITCHED_TAB,      // ✅ New value used by current code
        UNKNOWN_PERSON,
        AUDIO_DETECTED,
        EXAM_SUBMITTED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType;

    @Column(columnDefinition = "TEXT")
    private String eventMessage;

    @Column(name = "evidence_path")
    private String evidencePath;

    @Column(name = "event_time")
    private LocalDateTime eventTime;

    // ✅ Constructors
    public ProctorLog() {}

    public ProctorLog(Student student, Exam exam, EventType eventType, String eventMessage, String evidencePath, LocalDateTime eventTime) {
        this.student = student;
        this.exam = exam;
        this.eventType = eventType;
        this.eventMessage = eventMessage;
        this.evidencePath = evidencePath;
        this.eventTime = eventTime;
    }

    // ✅ Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventMessage() {
        return eventMessage;
    }

    public void setEventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
    }

    public String getEvidencePath() {
        return evidencePath;
    }

    public void setEvidencePath(String evidencePath) {
        this.evidencePath = evidencePath;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }
}
