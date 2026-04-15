package com.ai.universityassistant.dto;

import jakarta.validation.constraints.NotBlank;

public record AssistantRequest(
    @NotBlank(message = "message required")
    String message) {
}
