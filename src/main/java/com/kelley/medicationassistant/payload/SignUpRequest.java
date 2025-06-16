package com.kelley.medicationassistant.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * DTO declaring structure for sign up requests.
 * All users must sign up with a valid email address, a username, and a password.
 */
@Data
public class SignUpRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @Email
    private String email;
}
