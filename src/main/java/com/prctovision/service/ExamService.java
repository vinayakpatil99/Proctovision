package com.prctovision.service;

import com.prctovision.model.Exam;
import com.prctovision.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public Optional<Exam> getExamById(Long id) {
        return examRepository.findById(id);
    }

    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }

    public Exam updateExam(Exam exam) {
        return examRepository.save(exam);
    }

    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }

    public List<Exam> searchExams(String keyword) {
        return examRepository.findByTitleContainingIgnoreCase(keyword);
    }
}
