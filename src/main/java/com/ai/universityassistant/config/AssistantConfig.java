package com.ai.universityassistant.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssistantConfig {

    @Bean
    public ChatClient universityAssistant(ChatClient.Builder builder) {
        return builder
                .defaultSystem(AssistantConstants.DEFAULT_INSTRUCTIONS)
                .defaultToolNames(AssistantConstants.UNIVERSITY_TOOLS)
                .build();
    }
}
