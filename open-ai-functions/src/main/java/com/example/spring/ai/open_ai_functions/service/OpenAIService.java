package com.example.spring.ai.open_ai_functions.service;


import com.example.spring.ai.open_ai_functions.model.Answer;
import com.example.spring.ai.open_ai_functions.model.Question;

public interface OpenAIService {

    Answer getAnswer(Question question);

    Answer getStockPrice(Question question);

}
