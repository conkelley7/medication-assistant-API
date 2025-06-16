package com.kelley.medicationassistant.security.service;

import com.kelley.medicationassistant.exception.APIException;
import com.kelley.medicationassistant.model.User;
import com.kelley.medicationassistant.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Implementation of UserDetailsService, handles loading users from database and building
 * UserDetails objects for further use within Spring Security framework.
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username:" + username));

        org.springframework.security.core.userdetails.User.UserBuilder builder =
                org.springframework.security.core.userdetails.User.withUsername(username);

        builder.password(user.getPassword());
        builder.authorities(Collections.emptyList());

        return builder.build();
    }

    public UserDetails loadUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new APIException("User not found with email: " + email));

        org.springframework.security.core.userdetails.User.UserBuilder builder =
                org.springframework.security.core.userdetails.User.withUsername(user.getUsername());

        builder.password(user.getPassword());
        builder.authorities(Collections.emptyList());

        return builder.build();
    }
}
