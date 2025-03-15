package com.example.spring.ai.demo_spring_ai.services;

import com.example.spring.ai.demo_spring_ai.dto.Answer;
import com.example.spring.ai.demo_spring_ai.dto.GetCapitalRequest;
import com.example.spring.ai.demo_spring_ai.dto.GetCapitalResponse;
import com.example.spring.ai.demo_spring_ai.dto.Question;
import org.springframework.ai.moderation.ModerationResult;

public interface OpenAIService {
    String getAnswer(String question);

    Answer getAnswer(Question question);

    Answer getAnswerInMemoryAdvisor(Question question);

    float[] getEmbedding(String text);

    double textSimilarity(String text1, String text2);

    Answer getCapital(GetCapitalRequest getCapitalRequest);

    Answer getCapitalWithInfo(GetCapitalRequest getCapitalRequest);

    Answer getCapitalWithResponseJson(GetCapitalRequest getCapitalRequest);

    GetCapitalResponse getCapitalWithResponseJsonSchema(GetCapitalRequest getCapitalRequest);

    ModerationResult moderate(String text);
}
