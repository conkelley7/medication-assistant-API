package com.kelley.medicationassistant.security;

import com.kelley.medicationassistant.model.User;
import com.kelley.medicationassistant.repository.UserRepository;
import com.kelley.medicationassistant.security.exception.AuthEntryPoint;
import com.kelley.medicationassistant.security.service.AuthTokenFilter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class.
 *
 * This class defines the security filter chain, authentication manager,
 * password encoder, and a data initializer to create a default user.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthTokenFilter authTokenFilter;
    private final AuthEntryPoint authEntryPoint;
    private final UserRepository userRepository;

    public SecurityConfig(AuthTokenFilter authTokenFilter, AuthEntryPoint authEntryPoint,
                          UserRepository userRepository) {
        this.authTokenFilter = authTokenFilter;
        this.authEntryPoint = authEntryPoint;
        this.userRepository = userRepository;
    }

    /**
     * Configures HTTP security chain for the application, securing all endpoints
     * besides signup and login.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers ->
                        headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/user/signup").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Provides the AuthenticationManager bean to be used for authentication.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Provides bean for encoding passwords when saving to database.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Add a default user at runtime for ease of testing.
     */
    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername("DefaultUser")) {
                User user = new User();
                user.setUsername("DefaultUser");
                user.setPassword(passwordEncoder.encode("default123"));
                user.setEmail("defaultuser@example.com");
                userRepository.save(user);
            }
        };
    }
}
