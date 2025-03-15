package com.example.spring.ai.demo_spring_ai.services;

import com.example.spring.ai.demo_spring_ai.dto.Answer;
import com.example.spring.ai.demo_spring_ai.dto.GetCapitalRequest;
import com.example.spring.ai.demo_spring_ai.dto.GetCapitalResponse;
import com.example.spring.ai.demo_spring_ai.dto.Question;
import com.example.spring.ai.demo_spring_ai.dto.TestSimilarityRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.moderation.Moderation;
import org.springframework.ai.moderation.ModerationPrompt;
import org.springframework.ai.moderation.ModerationResult;
import org.springframework.ai.openai.OpenAiModerationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OpenAIServiceImpl implements OpenAIService{

    private final ChatModel chatModel;

    private final ChatClient chatClient;

    private final EmbeddingModel embeddingModel;

    private final OpenAiModerationModel moderationModel;

    public OpenAIServiceImpl(ChatModel chatModel, ChatClient.Builder chatClientBuilder, EmbeddingModel embeddingModel, OpenAiModerationModel moderationModel) {
        this.chatModel = chatModel;
        this.chatClient = chatClientBuilder.defaultAdvisors(
                new MessageChatMemoryAdvisor(new InMemoryChatMemory())).build();
        this.embeddingModel = embeddingModel;
        this.moderationModel = moderationModel;
    }

    @Autowired
    ObjectMapper objectMapper;

    @Value("classpath:templates/get-capital-prompt.st")
    private Resource getCapitalPrompt;

    @Value("classpath:templates/get-capital-with-info.st")
    private Resource getCapitalPromptWithInfo;

    @Value("classpath:templates/get-capital-with-response-json.st")
    private Resource getCapitalWithResponseJson;

    @Value("classpath:templates/get-capital-with-response-json-schema.st")
    private Resource getCapitalWithResponseJsonSchema;

    @Override
    public GetCapitalResponse getCapitalWithResponseJsonSchema(GetCapitalRequest getCapitalRequest) {
        BeanOutputConverter<GetCapitalResponse> converter = new BeanOutputConverter<>(GetCapitalResponse.class);
        String format = converter.getFormat();
        log.info("Format: {}", format);

        PromptTemplate promptTemplate = new PromptTemplate(getCapitalWithResponseJsonSchema);
        Prompt prompt = promptTemplate.create(Map.of(
                "stateOrCountry", getCapitalRequest.stateOrCountry(),
                "format", format));
        ChatResponse response = chatModel.call(prompt);

        String responseText = response.getResult().getOutput().getText();
        log.info("ResponseText: {}", responseText);

        return converter.convert(responseText);
    }

    @Override
    public Answer getCapitalWithResponseJson(GetCapitalRequest getCapitalRequest) {
        PromptTemplate promptTemplate = new PromptTemplate(getCapitalWithResponseJson);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", getCapitalRequest.stateOrCountry()));
        ChatResponse response = chatModel.call(prompt);

        String responseText = response.getResult().getOutput().getText();
        log.info(responseText);
        // responseText clear code block
        responseText = responseText.replaceAll("```json", "");
        responseText = responseText.replaceAll("```", "");
        log.info(responseText);
        String responseString;
        try {
            JsonNode jsonNode = objectMapper.readTree(responseText);
            responseString = jsonNode.get("answer").asText();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new Answer(responseString);
    }

    @Override
    public Answer getCapitalWithInfo(GetCapitalRequest getCapitalRequest) {
        PromptTemplate promptTemplate = new PromptTemplate(getCapitalPromptWithInfo);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", getCapitalRequest.stateOrCountry()));
        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());
    }

    @Override
    public Answer getCapital(GetCapitalRequest getCapitalRequest) {
        PromptTemplate promptTemplate = new PromptTemplate(getCapitalPrompt);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", getCapitalRequest.stateOrCountry()));
        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());
    }

    @Override
    public String getAnswer(String question) {
        PromptTemplate promptTemplate = new PromptTemplate(question);
        Prompt prompt = promptTemplate.create();

        ChatResponse response = chatModel.call(prompt);

        return response.getResult().getOutput().getText();
    }

    @Override
    public Answer getAnswer(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();
        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getText());
    }

    @Override
    public Answer getAnswerInMemoryAdvisor(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();
        ChatResponse response = chatClient.prompt(prompt).call().chatResponse();

        return new Answer(response.getResult().getOutput().getText());
    }

    @Override
    public float[] getEmbedding(String text) {
        return embeddingModel.embed(text);
    }

    @Override
    public double textSimilarity(String text1, String text2) {
        List<float[]> response = embeddingModel.embed(List.of(text1, text2));
        return cosineSimilarity(response.get(0), response.get(1));
    }

    private double cosineSimilarity(float[] vectorA, float[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("Vectors must be of the same length");
        }

        // Initialize variables for dot product and magnitudes
        double dotProduct = 0.0;
        double magnitudeA = 0.0;
        double magnitudeB = 0.0;

        // Calculate dot product and magnitudes
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            magnitudeA += vectorA[i] * vectorA[i];
            magnitudeB += vectorB[i] * vectorB[i];
        }

        // Calculate and return cosine similarity
        return dotProduct / (Math.sqrt(magnitudeA) * Math.sqrt(magnitudeB));
    }

    @Override
    public ModerationResult moderate(String text) {
        Moderation moderation = moderationModel.call(
                new ModerationPrompt(text)).getResult().getOutput();
        return moderation.getResults().get(0);

    }

    @Override
    public Flux<String> streamAnswer(String question) {

        return chatClient.prompt(question).stream().content();
    }
}
