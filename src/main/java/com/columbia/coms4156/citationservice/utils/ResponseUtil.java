package com.columbia.coms4156.citationservice.utils;

import com.columbia.coms4156.citationservice.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utility class for creating standardized HTTP responses.
 */
public final class ResponseUtil {

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private ResponseUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates a successful response with the given body and status.
     *
     * @param body the response body
     * @param status the HTTP status code
     * @param <T> the type of the response body
     * @return ResponseEntity with the body and status
     */
    public static <T> ResponseEntity<T> success(T body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }

    /**
     * Creates a successful response with HTTP 200 OK status.
     *
     * @param body the response body
     * @param <T> the type of the response body
     * @return ResponseEntity with the body and HTTP 200 status
     */
    public static <T> ResponseEntity<T> ok(T body) {
        return success(body, HttpStatus.OK);
    }

    /**
     * Creates a successful response with HTTP 201 CREATED status.
     *
     * @param body the response body
     * @param <T> the type of the response body
     * @return ResponseEntity with the body and HTTP 201 status
     */
    public static <T> ResponseEntity<T> created(T body) {
        return success(body, HttpStatus.CREATED);
    }

    /**
     * Creates a response with HTTP 204 NO CONTENT status.
     *
     * @return ResponseEntity with HTTP 204 status
     */
    public static ResponseEntity<Void> noContent() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Creates an error response.
     *
     * @param errorType the error type or category
     * @param message the detailed error message
     * @param status the HTTP status code
     * @param path the request path that caused the error
     * @return ResponseEntity containing ErrorResponse
     */
    public static ResponseEntity<ErrorResponse> error(String errorType, String message,
                                                      HttpStatus status, String path) {
        ErrorResponse error = new ErrorResponse(errorType, message, status.value(), path);
        return new ResponseEntity<>(error, status);
    }

    /**
     * Creates a bad request error response.
     *
     * @param message the detailed error message
     * @param path the request path that caused the error
     * @return ResponseEntity containing ErrorResponse with HTTP 400 status
     */
    public static ResponseEntity<ErrorResponse> badRequest(String message, String path) {
        return error("Bad Request", message, HttpStatus.BAD_REQUEST, path);
    }

    /**
     * Creates a not found error response.
     *
     * @param message the detailed error message
     * @param path the request path that caused the error
     * @return ResponseEntity containing ErrorResponse with HTTP 404 status
     */
    public static ResponseEntity<ErrorResponse> notFound(String message, String path) {
        return error("Not Found", message, HttpStatus.NOT_FOUND, path);
    }

    /**
     * Creates an internal server error response.
     *
     * @param message the detailed error message
     * @param path the request path that caused the error
     * @return ResponseEntity containing ErrorResponse with HTTP 500 status
     */
    public static ResponseEntity<ErrorResponse> internalServerError(String message,
                                                                    String path) {
        return error("Internal Server Error", message, HttpStatus.INTERNAL_SERVER_ERROR, path);
    }
}

