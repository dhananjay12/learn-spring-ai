package com.example.spring.ai.rag_with_milvus.service;


import com.example.spring.ai.rag_with_milvus.model.Answer;
import com.example.spring.ai.rag_with_milvus.model.Question;

public interface OpenAIService {

    Answer getAnswer(Question question);

}
