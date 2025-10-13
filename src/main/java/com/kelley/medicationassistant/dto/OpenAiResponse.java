package com.kelley.medicationassistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// TODO move to openai dto package package
/**
 * Format specified for responses from OpenAI API.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiResponse {
    private List<Choice> choices;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private ChatMessage message;
    }
}
