package com.ai.universityassistant.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.TimeZone;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ai.universityassistant.dto.AssistantResponse;

@SpringBootTest
public class AssistantServiceE2ETest {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Autowired
    private AssistantService assistantService;

    @Test
    void ask_shouldReturnNaturalLanguageResponse() {
        String userMessage = "Who are the top 2 students in Logic & Computation?";
        
        AssistantResponse response = assistantService.ask(userMessage);
        
        assertNotNull(response.answer());
        assertFalse(response.answer().isEmpty());
    }
}
