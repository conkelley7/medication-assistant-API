package com.kelley.medicationassistant.exception;

import com.kelley.medicationassistant.payload.APIResponse;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.Internal;
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

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> handleAPIException(APIException e, HttpServletRequest request) {
        APIResponse apiResponse = new APIResponse(
                e.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<APIResponse> handleFeignException(FeignException e, HttpServletRequest request) {

        globalExceptionLogger.error("Feign Error on Path {}: {}", request.getRequestURI(), e.getMessage());

        APIResponse apiResponse = new APIResponse(
                "An unexpected error has occured. Please try again later.",
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
