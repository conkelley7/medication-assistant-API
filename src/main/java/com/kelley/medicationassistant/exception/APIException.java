package com.kelley.medicationassistant.exception;

/**
 * Custom Exception class for generic API Exceptions
 * Most commonly used to set a custom message for errors that occur when validations fail (400 Bad Request)
 */
public class APIException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public APIException(String message) {
        super(message);
    }
}
