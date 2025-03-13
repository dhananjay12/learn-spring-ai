package com.example.spring.ai.open_ai_functions.service;


import com.example.spring.ai.open_ai_functions.functions.StockQuoteFunction;
import com.example.spring.ai.open_ai_functions.functions.WeatherServiceFunction;
import com.example.spring.ai.open_ai_functions.model.Answer;
import com.example.spring.ai.open_ai_functions.model.Question;
import com.example.spring.ai.open_ai_functions.model.StockPriceRequest;
import com.example.spring.ai.open_ai_functions.model.StockPriceResponse;
import com.example.spring.ai.open_ai_functions.model.WeatherRequest;
import com.example.spring.ai.open_ai_functions.model.WeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${my.aiapp.apiNinjasKey}")
    private String apiNinjasKey;

    private final OpenAiChatModel openAiChatModel;

    @Override
    public Answer getStockPrice(Question question) {
        var promptOptions = OpenAiChatOptions.builder()
                .toolCallbacks(List.of(
                        FunctionToolCallback.builder("CurrentStockPrice", new StockQuoteFunction(apiNinjasKey)
                                )
                        .description("Get the current stock price for a stock symbol")
                        .toolCallResultConverter((response, String) -> {
                            String schema = ModelOptionsUtils.getJsonSchema(StockPriceResponse.class, false);
                            String json = ModelOptionsUtils.toJsonString(response);
                            return schema + "\n" + json;
                        })
                        .inputType(StockPriceRequest.class)
                        .build()))
                .build();

        Message userMessage = new PromptTemplate(question.question()).createMessage();
        Message systemMessage = new SystemPromptTemplate("You are an agent which returns back a stock price for the given stock symbol (or ticker)").createMessage();

        var response = openAiChatModel.call(new Prompt(List.of(userMessage, systemMessage), promptOptions));

        return new Answer(response.getResult().getOutput().getText());
    }

    @Override
    public Answer getAnswer(Question question) {
        var promptOptions = OpenAiChatOptions.builder()
                .toolCallbacks(List.of(
                        FunctionToolCallback.builder("CurrentWeather", new WeatherServiceFunction(apiNinjasKey)
                                )
                        .description("Get the current weather for a location")
                        .inputType(WeatherRequest.class)
                        .toolCallResultConverter((response, String) -> {
                            String schema = ModelOptionsUtils.getJsonSchema(WeatherResponse.class, false);
                            String json = ModelOptionsUtils.toJsonString(response);
                            return schema + "\n" + json;
                        })
                        .build()))
                .build();

        Message userMessage = new PromptTemplate(question.question()).createMessage();

        var response = openAiChatModel.call(new Prompt(List.of(userMessage), promptOptions));

        return new Answer(response.getResult().getOutput().getText());
    }
}
