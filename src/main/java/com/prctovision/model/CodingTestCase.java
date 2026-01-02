package com.prctovision.model;

import jakarta.persistence.*;

@Entity
@Table(name = "coding_test_cases")
public class CodingTestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Question question;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String inputData;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String expectedOutput;

    private boolean isHidden = true;

    // Getters and Setters
    public Long getId() { return id; }
    public Question getQuestion() { return question; }
    public void setQuestion(Question question) { this.question = question; }
    public String getInputData() { return inputData; }
    public void setInputData(String inputData) { this.inputData = inputData; }
    public String getExpectedOutput() { return expectedOutput; }
    public void setExpectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; }
    public boolean isHidden() { return isHidden; }
    public void setHidden(boolean hidden) { isHidden = hidden; }
}
