package com.ai.universityassistant.dto;

import java.util.Map;

public record ErrorResponse(
    int status,
    String message,
    long timestamp,
    Map<String, String> errors) {

    public ErrorResponse(int status, String message, long timestamp) {
        this(status, message, timestamp, null);
    }
}
