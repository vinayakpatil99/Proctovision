package com.prctovision.repository;

import com.prctovision.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByTitleContainingIgnoreCase(String title);
}
