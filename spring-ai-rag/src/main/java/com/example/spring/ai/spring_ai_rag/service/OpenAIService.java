package com.example.spring.ai.spring_ai_rag.service;

import com.example.spring.ai.spring_ai_rag.model.Answer;
import com.example.spring.ai.spring_ai_rag.model.Question;

public interface OpenAIService {

    Answer getAnswer(Question question);

}
