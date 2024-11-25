package com.example.ai.serviceImpl;

import com.example.ai.service.IAiService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AiServiceImpl implements IAiService {

    @Value("${ai.ollama.chat.options.model}")
    String ollamaModel;

    @Override
    public List<String> generateKeywords(String prompt) {
        var ollamaApi = new OllamaApi();

        var chatModel = new OllamaChatModel(ollamaApi,
                OllamaOptions.create()
                        .withModel(ollamaModel)
                        .withTemperature(0.9F));

        String promptWithInstruction = "Generate a maximum of 10 keywords related to the sentence or topic I provide.\n" +
                "Keywords can include topics, careers, or categories. Please avoid common words such as 'here', 'are', 'the', 'and', etc.\n" +
                "For example, if I say 'I am sick', you might return ['doctor', 'nurse']. Now, for the sentence: '" + prompt + "' what will you generate?";

        ChatResponse response = chatModel.call(new Prompt(promptWithInstruction));

        String content = response.getResult().getOutput().getContent();

        List<String> keywords = extractKeywordsFromContent(content);

        return keywords;
    }

    @Override
    public List<String> generateKeywordsWithTranslations(String prompt) {
        var ollamaApi = new OllamaApi();

        var chatModel = new OllamaChatModel(ollamaApi,
                OllamaOptions.create()
                        .withModel("gemma2")
                        .withTemperature(0.9F));

        String promptWithInstruction = "Generate a maximum of 10 keywords related to the sentence or topic I provide.\n" +
                "Keywords can include topics, careers, or categories. Please avoid common words such as 'here', 'are', 'the', 'and', etc.\n" +
                "For example, if I say 'I am sick', you might return ['doctor', 'nurse']. Now, for the sentence: '" + prompt + "' what will you generate?";

        ChatResponse response = chatModel.call(new Prompt(promptWithInstruction));

        String content = response.getResult().getOutput().getContent();

        List<String> keywords = extractKeywordsFromContent(content);

        String translatedKeywords = translateKeywords(keywords);

        List<String> translatedKeywordsList = extractKeywordsFromContent(translatedKeywords);

        return translatedKeywordsList;
    }

    private String translateKeywords(List<String> keywords) {
        var ollamaApi = new OllamaApi();

        var chatModel = new OllamaChatModel(ollamaApi,
                OllamaOptions.create()
                        .withModel("gemma2")
                        .withTemperature(0.9F));

        String keywordList = String.join(", ", keywords);

        String promptWithInstruction = "Can you convert the following English words into both Chinese and Thai, " +
                "and return as a single array? Just give me back a single array without any other words. " +
                "Here's the array of words to translate: [" + keywordList + "]";

        ChatResponse response = chatModel.call(new Prompt(promptWithInstruction));

        String translatedKeywords = response.getResult().getOutput().getContent();

        return translatedKeywords;
    }

    private List<String> extractKeywordsFromContent(String content) {
        Set<String> stopWords = Set.of("here", "are", "some", "the", "and", "like", "can", "let", "you", "further", "know", "keywords", "related", "want", "name");

        String[] words = content.split("[^a-zA-Z\\u4e00-\\u9fa5\\u0E00-\\u0E7F]+");

        List<String> keywords = new ArrayList<>();

        for (String word : words) {
            if (word.length() > 2 && !stopWords.contains(word.toLowerCase())) {
                keywords.add(word.toLowerCase());
            }
        }

        return keywords.stream().limit(10).collect(Collectors.toList());
    }
}
