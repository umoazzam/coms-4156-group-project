package com.columbia.coms4156.citationservice.exception;

import com.columbia.coms4156.citationservice.model.ErrorResponse;
import com.columbia.coms4156.citationservice.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Global exception handler for all controllers.
 * Centralizes exception handling and provides consistent error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /** Logger for this class. */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles ResourceNotFoundException.
     *
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        LOGGER.warn("Resource not found: {}", ex.getMessage());
        return ResponseUtil.notFound(ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handles ValidationException.
     *
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex, HttpServletRequest request) {
        LOGGER.warn("Validation error: {}", ex.getMessage());
        return ResponseUtil.badRequest(ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handles IllegalArgumentException.
     *
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        LOGGER.warn("Illegal argument: {}", ex.getMessage());
        return ResponseUtil.badRequest(ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handles MethodArgumentNotValidException (validation errors from @Valid).
     *
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(error.getField())
                    .append(" - ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }
        LOGGER.warn("Validation error: {}", errorMessage.toString());
        return ResponseUtil.badRequest(errorMessage.toString(), request.getRequestURI());
    }

    /**
     * Handles all other exceptions.
     *
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        LOGGER.error("Unexpected error occurred", ex);
        String message = "An unexpected error occurred: " + ex.getMessage();
        return ResponseUtil.internalServerError(message, request.getRequestURI());
    }
}

