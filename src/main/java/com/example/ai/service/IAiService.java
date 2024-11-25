package com.example.ai.service;

import java.util.List;

public interface IAiService {
    List<String> generateKeywords(String prompt);

    List<String> generateKeywordsWithTranslations(String prompt);
}
