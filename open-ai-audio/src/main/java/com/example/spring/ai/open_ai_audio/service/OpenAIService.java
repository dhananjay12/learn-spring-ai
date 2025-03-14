package com.example.spring.ai.open_ai_audio.service;

import com.example.spring.ai.open_ai_audio.model.Question;
import org.springframework.web.multipart.MultipartFile;

public interface OpenAIService {

    byte[] getSpeech(Question question);

    String getTranscript(MultipartFile file);
}
