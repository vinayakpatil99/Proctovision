package com.prctovision.repository;

import com.prctovision.model.CodingTestCase;
import com.prctovision.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CodingTestCaseRepository extends JpaRepository<CodingTestCase, Long> {

    // Fetch all coding test cases for a given question
    List<CodingTestCase> findByQuestion(Question question);
}
