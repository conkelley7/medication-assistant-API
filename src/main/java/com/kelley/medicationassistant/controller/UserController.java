package com.kelley.medicationassistant.controller;

import com.kelley.medicationassistant.payload.SignUpRequest;
import com.kelley.medicationassistant.payload.UserResponse;
import com.kelley.medicationassistant.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest Controller class, exposes API endpoints relating to user operations.
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles user registration by processing the sign-up request.
     *
     * @param signUpRequest The sign-up request containing user information.
     * @return ResponseEntity containing UserResponse DTO (with user information) and HTTP status.
     */
    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        UserResponse userResponse = userService.signUp(signUpRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    /**
     * Deletes a user based on the provided username.
     *
     * @param username The username of the user to be deleted.
     * @return ResponseEntity containing the UserResponse DTO (with user information) and HTTP status.
     */
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<UserResponse> deleteUserByUsername(@PathVariable String username) {
        UserResponse userResponse = userService.deleteUserByUsername(username);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }


}