package com.kelley.medicationassistant.security.controller;

import com.kelley.medicationassistant.exception.APIException;
import com.kelley.medicationassistant.model.User;
import com.kelley.medicationassistant.payload.UserResponse;
import com.kelley.medicationassistant.repository.UserRepository;
import com.kelley.medicationassistant.security.payload.LoginRequest;
import com.kelley.medicationassistant.security.payload.LoginResponse;
import com.kelley.medicationassistant.security.service.JwtService;
import com.kelley.medicationassistant.security.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager,
                          UserDetailsServiceImpl userDetailsService, UserRepository userRepository,
                          ModelMapper modelMapper) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
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

    @GetMapping("/current")
    public ResponseEntity<UserResponse> getCurrentLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new APIException("No logged in user found"));

        UserResponse response = modelMapper.map(user, UserResponse.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
