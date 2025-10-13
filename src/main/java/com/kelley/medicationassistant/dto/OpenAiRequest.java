package com.kelley.medicationassistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// TODO move to openai dto package package
/**
 * Format specified for sending chat requests to OpenAI API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiRequest {
    private String model;
    private List<ChatMessage> messages;
    private double temperature = 0.3;
}
