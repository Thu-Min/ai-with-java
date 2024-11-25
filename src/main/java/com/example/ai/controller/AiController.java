package com.example.ai.controller;

import com.example.ai.service.IAiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final IAiService aiService;

    public AiController(IAiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/generate-keywords")
    public List<String> generateKeywords(@RequestBody String prompt) {
        return this.aiService.generateKeywords(prompt);
    }

    @PostMapping("/generate-keywords-with-translations")
    public List<String> generateKeywordsWithTranslations(@RequestBody String prompt) {
        return this.aiService.generateKeywordsWithTranslations(prompt);
    }
}
