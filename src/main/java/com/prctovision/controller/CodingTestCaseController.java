package com.prctovision.controller;

import com.prctovision.model.CodingTestCase;
import com.prctovision.model.Question;
import com.prctovision.repository.CodingTestCaseRepository;
import com.prctovision.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/coding-test-cases")
public class CodingTestCaseController {

    @Autowired
    private CodingTestCaseRepository testCaseRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @GetMapping("/question/{questionId}")
    public List<CodingTestCase> getByQuestion(@PathVariable Long questionId) {
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        return testCaseRepo.findByQuestion(question);
    }

    @PostMapping("/question/{questionId}")
    public CodingTestCase createTestCase(@PathVariable Long questionId, @RequestBody CodingTestCase testCase) {
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        testCase.setQuestion(question);
        return testCaseRepo.save(testCase);
    }

    @PutMapping("/{id}")
    public CodingTestCase updateTestCase(@PathVariable Long id, @RequestBody CodingTestCase updated) {
        return testCaseRepo.findById(id).map(tc -> {
            tc.setInputData(updated.getInputData());
            tc.setExpectedOutput(updated.getExpectedOutput());
            tc.setHidden(updated.isHidden());
            return testCaseRepo.save(tc);
        }).orElseThrow(() -> new RuntimeException("Test case not found"));
    }

    @DeleteMapping("/{id}")
    public String deleteTestCase(@PathVariable Long id) {
        testCaseRepo.deleteById(id);
        return "Coding test case deleted successfully";
    }
}
