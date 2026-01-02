package com.prctovision.controller;

import com.prctovision.model.Question;
import com.prctovision.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepo;

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    @PostMapping
    public Question createQuestion(@RequestBody Question question) {
        return questionRepo.save(question);
    }

    @PutMapping("/{id}")
    public Question updateQuestion(@PathVariable Long id, @RequestBody Question updated) {
        return questionRepo.findById(id).map(q -> {
            q.setTitle(updated.getTitle());
            q.setType(updated.getType());
            q.setOptions(updated.getOptions());
            q.setAnswer(updated.getAnswer());
            return questionRepo.save(q);
        }).orElseThrow(() -> new RuntimeException("Question not found"));
    }

    @DeleteMapping("/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        questionRepo.deleteById(id);
        return "Question deleted successfully";
    }
}
