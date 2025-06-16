package com.kelley.medicationassistant.controller;

import com.kelley.medicationassistant.payload.SignUpRequest;
import com.kelley.medicationassistant.payload.UserResponse;
import com.kelley.medicationassistant.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        UserResponse userResponse = userService.signUp(signUpRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<UserResponse> deleteUserByUsername(@PathVariable String username) {
        UserResponse userResponse = userService.deleteUserByUsername(username);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }


}