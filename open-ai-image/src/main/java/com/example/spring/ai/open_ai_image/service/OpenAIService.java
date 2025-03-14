package com.example.spring.ai.open_ai_image.service;


import com.example.spring.ai.open_ai_image.model.Question;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OpenAIService {

    byte[] getImage(Question question);

    String getDescription(MultipartFile file) throws IOException;

}
