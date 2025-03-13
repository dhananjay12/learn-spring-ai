package com.example.spring.ai.open_ai_functions.controller;


import com.example.spring.ai.open_ai_functions.model.Answer;
import com.example.spring.ai.open_ai_functions.model.Question;
import com.example.spring.ai.open_ai_functions.service.OpenAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class QuestionController {

    private final OpenAIService openAIService;

    @PostMapping("/stock")
    public Answer getStockPrice(@RequestBody Question question) {
        return openAIService.getStockPrice(question);
    }

    @PostMapping("/weather")
    public Answer weather(@RequestBody Question question) {
        return openAIService.getAnswer(question);
    }

}