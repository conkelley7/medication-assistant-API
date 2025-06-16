package com.kelley.medicationassistant.service;

import com.kelley.medicationassistant.payload.SignUpRequest;
import com.kelley.medicationassistant.payload.UserResponse;
import jakarta.validation.Valid;

/**
 * Interface defining methods for handling services related to users
 */
public interface UserService {
    /**
     * Service method to handle logic related to user sign up.
     *
     * @param signUpRequest containing sign up details, such as username, password, and email
     * @return UserResponse DTO containing details of registered user
     */
    UserResponse signUp(@Valid SignUpRequest signUpRequest);

    /**
     * Service method to handle deleting user accounts
     *
     * @param username username of the account to be deleted
     * @return UserResponse DTO containing the details of the deleted user
     */
    UserResponse deleteUserByUsername(String username);
}
