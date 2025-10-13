package com.kelley.medicationassistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper class for Exception Handling
 * See {@link com.kelley.medicationassistant.exception.GlobalExceptionHandler}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {
    private String message;
    private String path;
}
