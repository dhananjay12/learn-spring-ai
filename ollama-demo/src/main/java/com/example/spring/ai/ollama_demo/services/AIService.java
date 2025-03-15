package com.example.spring.ai.ollama_demo.services;


import com.example.spring.ai.ollama_demo.dto.Answer;
import com.example.spring.ai.ollama_demo.dto.GetCapitalRequest;
import com.example.spring.ai.ollama_demo.dto.GetCapitalResponse;
import com.example.spring.ai.ollama_demo.dto.Question;

public interface AIService {
    String getAnswer(String question);

    Answer getAnswer(Question question);

    Answer getAnswerInMemoryAdvisor(Question question);

    float[] getEmbedding(String text);

    double textSimilarity(String text1, String text2);

    Answer getCapital(GetCapitalRequest getCapitalRequest);
}
