package com.example.spring.ai.rag_with_milvus.controller;


import com.example.spring.ai.rag_with_milvus.model.Answer;
import com.example.spring.ai.rag_with_milvus.model.Question;
import com.example.spring.ai.rag_with_milvus.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final OpenAIService openAIService;

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return openAIService.getAnswer(question);
    }

}