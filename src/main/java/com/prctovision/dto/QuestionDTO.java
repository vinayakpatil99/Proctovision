package com.prctovision.dto;

import java.util.ArrayList;
import java.util.List;

public class QuestionDTO {

    private Long id;
    private Long examId;
    private String examTitle;
    private String title;
    private String type;
    private List<String> options = new ArrayList<>();

    public QuestionDTO() {}

    public QuestionDTO(Long id, Long examId, String examTitle, String title, String type, List<String> options) {
        this.id = id;
        this.examId = examId;
        this.examTitle = examTitle;
        this.title = title;
        this.type = type;
        this.options = (options != null) ? options : new ArrayList<>();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) {
        this.options = (options != null) ? options : new ArrayList<>();
    }
}
