package com.prctovision.service;

import com.prctovision.model.Result;
import com.prctovision.model.Student;
import com.prctovision.model.Exam;
import com.prctovision.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    // ✅ Fetch all results (for Admin Dashboard)
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    // ✅ Get result by result ID
    public Optional<Result> getResultById(Long id) {
        return resultRepository.findById(id);
    }

    // ✅ Get all results for a specific student
    public List<Result> getResultsByStudent(Student student) {
        return resultRepository.findByStudent(student);
    }

    // ✅ Get all results for a specific exam
    public List<Result> getResultsByExam(Exam exam) {
        return resultRepository.findByExam(exam);
    }

    // ✅ Get all results by studentId
    public List<Result> getResultsByStudentId(Long studentId) {
        return resultRepository.findByStudentId(studentId);
    }

    // ✅ Get all results by examId
    public List<Result> getResultsByExamId(Long examId) {
        return resultRepository.findByExamId(examId);
    }

    // ✅ Get specific result (Student + Exam combination)
    public Optional<Result> getResultByStudentAndExam(Student student, Exam exam) {
        return resultRepository.findByStudentAndExam(student, exam);
    }

    // ✅ Save or update a result
    public Result saveResult(Result result) {
        return resultRepository.save(result);
    }

    // ✅ Delete a result record
    public void deleteResult(Long id) {
        resultRepository.deleteById(id);
    }
}
