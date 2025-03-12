package com.example.spring_ai_rag.service;

import com.example.spring_ai_rag.model.Answer;
import com.example.spring_ai_rag.model.Question;

public interface OpenAIService {

    Answer getAnswer(Question question);

}
