package com.kelley.medicationassistant.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChatMessage DTO, following format expected by OpenAI API.
 * {@link OpenAiRequest} is to contain a list of Chat Messages, with role and content values for each message.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String role;
    private String content;
}
