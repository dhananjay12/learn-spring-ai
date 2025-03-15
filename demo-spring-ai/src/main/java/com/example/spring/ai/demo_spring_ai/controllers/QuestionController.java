package com.example.spring.ai.demo_spring_ai.controllers;

import com.example.spring.ai.demo_spring_ai.dto.Answer;
import com.example.spring.ai.demo_spring_ai.dto.GetCapitalRequest;
import com.example.spring.ai.demo_spring_ai.dto.GetCapitalResponse;
import com.example.spring.ai.demo_spring_ai.dto.Question;
import com.example.spring.ai.demo_spring_ai.dto.TestSimilarityRequest;
import com.example.spring.ai.demo_spring_ai.services.OpenAIService;
import org.springframework.ai.moderation.ModerationResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class QuestionController {

    private final OpenAIService openAIService;

    public QuestionController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return openAIService.getAnswer(question);
    }

    @PostMapping("/embedding")
    public float[] embedding(@RequestBody String text) {
        return openAIService.getEmbedding(text);
    }

    @PostMapping("/textSimilarity")
    public double textSimilarity(@RequestBody TestSimilarityRequest testSimilarityRequest) {
        return openAIService.textSimilarity(testSimilarityRequest.text1(),
                testSimilarityRequest.text2());
    }

    @PostMapping("/askInMemory")
    public Answer askQuestionInMemory(@RequestBody Question question) {
        return openAIService.getAnswerInMemoryAdvisor(question);
    }

    @PostMapping("/capital")
    public Answer getCapital(@RequestBody GetCapitalRequest getCapitalRequest) {
        return this.openAIService.getCapital(getCapitalRequest);
    }

    @PostMapping("/capitalWithInfo")
    public Answer getCapitalWithInfo(@RequestBody GetCapitalRequest getCapitalRequest) {
        return this.openAIService.getCapitalWithInfo(getCapitalRequest);
    }

    @PostMapping("/capitalWithResponseJson")
    public Answer getCapitalWithResponseJson(@RequestBody GetCapitalRequest getCapitalRequest) {
        return this.openAIService.getCapitalWithResponseJson(getCapitalRequest);
    }

    @PostMapping("/capitalWithResponseJsonSchema")
    public GetCapitalResponse getCapitalWithResponseJsonSchema(@RequestBody GetCapitalRequest getCapitalRequest) {
        return this.openAIService.getCapitalWithResponseJsonSchema(getCapitalRequest);
    }

    @PostMapping("/checkModeration")
    public ModerationResult checkModeration(@RequestBody String text) {
        return this.openAIService.moderate(text);
    }
}