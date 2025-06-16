package com.kelley.medicationassistant.security.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * DTO that enables structure for login requests.
 * Users can either log in with email or username, and their password.
 */
@Data
public class LoginRequest {
    private String username;
    private String email;
    @NotEmpty
    private String password;
}
