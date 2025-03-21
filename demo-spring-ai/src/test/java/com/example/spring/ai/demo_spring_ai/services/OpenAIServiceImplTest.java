package com.example.spring.ai.demo_spring_ai.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenAIServiceImplTest {

    @Autowired
    OpenAIService openAIService;

    @Test
    void getAnswer() {

        String answer = openAIService.getAnswer("Tell me a dad joke.");
        System.out.println("Got the answer");
        System.out.println(answer);
    }
}
