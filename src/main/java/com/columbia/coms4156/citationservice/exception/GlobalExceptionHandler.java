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

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("Resource not found: {}", ex.getMessage());
        }
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
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("Validation error: {}", ex.getMessage());
        }
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
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("Illegal argument: {}", ex.getMessage());
        }
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
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("Validation error: {}", errorMessage.toString());
        }
        return ResponseUtil.badRequest(errorMessage.toString(), request.getRequestURI());
    }

    /**
     * Handles HttpMessageNotReadableException (e.g., malformed JSON).
     *
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("Malformed JSON request: {}", ex.getMessage());
        }
        return ResponseUtil.badRequest("Malformed JSON request: " + ex.getMessage(),
                request.getRequestURI());
    }

    /**
     * Handles HttpRequestMethodNotSupportedException.
     *
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("Method not allowed: {}", ex.getMessage());
        }
        return ResponseUtil.error("Method Not Allowed", ex.getMessage(),
                org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED,
                request.getRequestURI());
    }

    /**
     * Handles HttpMediaTypeNotSupportedException.
     *
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("Unsupported media type: {}", ex.getMessage());
        }
        return ResponseUtil.error("Unsupported Media Type", ex.getMessage(),
                org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                request.getRequestURI());
    }

    /**
     * Handles MethodArgumentTypeMismatchException
     * (e.g., failed to convert path variable to expected type).
     *
     * @param ex the exception
     * @param request the web request
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn("Method argument type mismatch: {}", ex.getMessage());
        }
        String errorMessage = String.format(
                "Invalid parameter '%s': Value '%s' could not be converted to type %s.",
                ex.getName(), ex.getValue(),
                ex.getRequiredType() != null
                        ? ex.getRequiredType().getSimpleName() : "unknown");
        return ResponseUtil.badRequest(errorMessage, request.getRequestURI());
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
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("Unexpected error occurred", ex);
        }
        String message = "An unexpected error occurred: " + ex.getMessage();
        return ResponseUtil.internalServerError(message, request.getRequestURI());
    }
}

