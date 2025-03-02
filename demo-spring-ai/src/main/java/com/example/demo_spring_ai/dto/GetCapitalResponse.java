package com.example.demo_spring_ai.dto;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record GetCapitalResponse (
        @JsonPropertyDescription("This is the city name") String answer) {
}
