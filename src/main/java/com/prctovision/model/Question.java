package com.prctovision.model;

import com.prctovision.model.enums.QuestionType;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String title;

    @Enumerated(EnumType.STRING)
    private QuestionType type; // MCQ, CODING

    @ElementCollection
    private List<String> options; // For MCQs

    private String answer;

    @ManyToOne
    private Exam exam;

    // Getters and Setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public QuestionType getType() { return type; }
    public void setType(QuestionType type) { this.type = type; }
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }
}
