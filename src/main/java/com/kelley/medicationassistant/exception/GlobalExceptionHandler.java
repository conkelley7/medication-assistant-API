package com.kelley.medicationassistant.exception;

import com.kelley.medicationassistant.payload.APIResponse;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles exception responses for RestController classes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger globalExceptionLogger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles API-specific exceptions (APIException) and provides a structured error response.
     * APIExceptions are thrown whenever validation requirements are not met, so a 400 Bad Request response is
     * most appropriate.
     *
     * @param e The APIException that was thrown.
     * @param request The HTTP request that triggered the exception, used to capture the URI.
     * @return ResponseEntity containing the error response and appropriate HTTP status (BAD_REQUEST).
     */
    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> handleAPIException(APIException e, HttpServletRequest request) {
        APIResponse apiResponse = new APIResponse(
                e.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles Feign-specific exceptions (FeignException) and logs the error.
     * Provides a generic error message to the client and returns a 500 Internal Server Error status.
     *
     * @param e The FeignException that was thrown.
     * @param request The HTTP request that triggered the exception, used to capture the URI.
     * @return ResponseEntity containing a generic error message and HTTP status (INTERNAL_SERVER_ERROR).
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<APIResponse> handleFeignException(FeignException e, HttpServletRequest request) {

        globalExceptionLogger.error("Feign Error on Path {}: {}", request.getRequestURI(), e.getMessage());

        APIResponse apiResponse = new APIResponse(
                "An unexpected error has occurred. Please try again later.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
