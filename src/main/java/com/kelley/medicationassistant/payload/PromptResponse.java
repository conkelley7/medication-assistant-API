package com.kelley.medicationassistant.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ResponseDTO for medication information requests, wraps OpenAI API response string.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromptResponse {
    private String message;
}
