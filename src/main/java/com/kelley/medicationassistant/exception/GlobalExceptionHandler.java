package com.kelley.medicationassistant.exception;

import com.kelley.medicationassistant.payload.APIResponse;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles exception responses for RestController classes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> handleAPIException(APIException e, HttpServletRequest request) {
        APIResponse apiResponse = new APIResponse(
                e.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
