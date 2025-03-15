package com.example.spring.ai.ollama_demo.controllers;

import com.example.spring.ai.ollama_demo.dto.Answer;
import com.example.spring.ai.ollama_demo.dto.GetCapitalRequest;
import com.example.spring.ai.ollama_demo.dto.Question;
import com.example.spring.ai.ollama_demo.dto.TestSimilarityRequest;
import com.example.spring.ai.ollama_demo.services.AIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class QuestionController {

    private final AIService aiService;

    public QuestionController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return aiService.getAnswer(question);
    }

    @PostMapping("/embedding")
    public float[] embedding(@RequestBody String text) {
        return aiService.getEmbedding(text);
    }

    @PostMapping("/textSimilarity")
    public double textSimilarity(@RequestBody TestSimilarityRequest testSimilarityRequest) {
        return aiService.textSimilarity(testSimilarityRequest.text1(),
                testSimilarityRequest.text2());
    }

    @PostMapping("/askInMemory")
    public Answer askQuestionInMemory(@RequestBody Question question) {
        return aiService.getAnswerInMemoryAdvisor(question);
    }

    @PostMapping("/capital")
    public Answer getCapital(@RequestBody GetCapitalRequest getCapitalRequest) {
        return this.aiService.getCapital(getCapitalRequest);
    }

}