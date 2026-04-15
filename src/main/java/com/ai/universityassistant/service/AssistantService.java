package com.ai.universityassistant.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.ai.universityassistant.dto.AssistantResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssistantService {

    private final ChatClient universityAssistant;

    public AssistantResponse ask(String message) {
        var answer = universityAssistant.prompt()
                .user(message)
                .call()
                .content();

        return new AssistantResponse(answer);
    }
}
