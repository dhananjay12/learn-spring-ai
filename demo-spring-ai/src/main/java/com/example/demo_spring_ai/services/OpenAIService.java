package com.example.demo_spring_ai.services;

import com.example.demo_spring_ai.dto.Answer;
import com.example.demo_spring_ai.dto.GetCapitalRequest;
import com.example.demo_spring_ai.dto.GetCapitalResponse;
import com.example.demo_spring_ai.dto.Question;

public interface OpenAIService {
    String getAnswer(String question);

    Answer getAnswer(Question question);

    Answer getCapital(GetCapitalRequest getCapitalRequest);

    Answer getCapitalWithInfo(GetCapitalRequest getCapitalRequest);

    Answer getCapitalWithResponseJson(GetCapitalRequest getCapitalRequest);

    GetCapitalResponse getCapitalWithResponseJsonSchema(GetCapitalRequest getCapitalRequest);
}
