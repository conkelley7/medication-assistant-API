package com.kelley.medicationassistant.security.controller;

import com.kelley.medicationassistant.exception.APIException;
import com.kelley.medicationassistant.security.payload.LoginRequest;
import com.kelley.medicationassistant.security.payload.LoginResponse;
import com.kelley.medicationassistant.security.service.JwtService;
import com.kelley.medicationassistant.security.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager,
                          UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        if (loginRequest.getUsername() == null && loginRequest.getEmail() == null)
            throw new APIException("Login request must contain either a username or an email");

        UserDetails userDetails;
        // Set userDetails based on the choice of login method (username or email)
        if (loginRequest.getUsername() != null) {
            userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        } else {
            userDetails = userDetailsService.loadUserByEmail(loginRequest.getEmail());
        }

        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername() != null ? loginRequest.getUsername() : loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        Authentication auth = authenticationManager.authenticate(credentials);

        String jwt = jwtService.generateToken(userDetails);
        LoginResponse loginResponse = new LoginResponse(
                "success",
                userDetails.getUsername()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .body(loginResponse);
    }
}
