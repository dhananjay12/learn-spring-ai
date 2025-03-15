package com.example.spring.ai.ollama_demo.dto;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record GetCapitalResponse (
        @JsonPropertyDescription("This is the city name") String answer) {
}
