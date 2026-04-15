package com.ai.universityassistant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai.universityassistant.dto.AssistantRequest;
import com.ai.universityassistant.dto.AssistantResponse;
import com.ai.universityassistant.service.AssistantService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AssistantController {

    private final AssistantService assistantService;

    @Operation(
        summary = "Ask a question about the university", 
        description = "Submit a question in plain English to receive a helpful response based on current " +
            "university records and statistics."
    ) 
    @PostMapping("/ask")
    public ResponseEntity<AssistantResponse> ask(@RequestBody @Valid AssistantRequest request) {
        return ResponseEntity.ok(assistantService.ask(request.message()));
    }
}
