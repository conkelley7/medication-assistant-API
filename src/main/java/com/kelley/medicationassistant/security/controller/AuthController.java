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
import org.springframework.web.bind.annotation.*;


/**
 * AuthController is responsible for handling user authentication and related operations
 * such as logging in and retrieving the current logged-in user's details.
 */
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

    /**
     * Handles user login by authenticating the user and returning a JWT token.
     *
     * @param loginRequest The login request containing either the username or email and password.
     * @return ResponseEntity containing LoginResponse DTO (username and status), and JWT token.
     * @throws APIException if neither username nor email is provided in the request.
     */
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

    /**
     * Retrieves the details of the currently authenticated user.
     *
     * @return ResponseEntity containing the user's details as a UserResponse DTO.
     * @throws APIException if no authenticated user is found in the security context.
     */
    @GetMapping("/current")
    public ResponseEntity<UserResponse> getCurrentLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new APIException("No logged in user found"));

        UserResponse response = modelMapper.map(user, UserResponse.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
