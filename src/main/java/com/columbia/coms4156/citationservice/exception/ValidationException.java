package com.columbia.coms4156.citationservice.exception;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs a new ValidationException with the specified message.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ValidationException with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

