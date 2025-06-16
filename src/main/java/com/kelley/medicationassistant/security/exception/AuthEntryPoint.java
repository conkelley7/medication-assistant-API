package com.kelley.medicationassistant.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * Implementation of Spring Security's AuthenticationEntryPoint, used to handle authentication errors.
 */
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private final Logger authExceptionLogger = LoggerFactory.getLogger(AuthEntryPoint.class);

    /**
     * Handles unauthorized access attempts to protected resources by providing an HTTP 401 Unauthorized response.
     * Constructs a JSON response body containing details about the unauthorized access, including the status,
     * error type, exception message, and the request path.
     *
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        authExceptionLogger.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
