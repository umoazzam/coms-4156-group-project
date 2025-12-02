package com.columbia.coms4156.citationservice.model;

import java.time.LocalDateTime;

/**
 * Model class for standardized error responses.
 * Provides consistent structure for error messages across all API endpoints.
 */
public class ErrorResponse {

    /** The error type or category. */
    private String error;

    /** The detailed error message. */
    private String message;

    /** The HTTP status code. */
    private int status;

    /** The request path that caused the error. */
    private String path;

    /** The timestamp when the error occurred. */
    private LocalDateTime timestamp;

    /**
     * Default constructor.
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor with error details.
     *
     * @param errorType the error type
     * @param errorMessage the detailed error message
     * @param statusCode the HTTP status code
     * @param requestPath the request path that caused the error
     */
    public ErrorResponse(String errorType, String errorMessage, int statusCode,
                         String requestPath) {
        this();
        this.error = errorType;
        this.message = errorMessage;
        this.status = statusCode;
        this.path = requestPath;
    }

    /**
     * Gets the error type.
     *
     * @return the error type
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error type.
     *
     * @param errorType the error type
     */
    public void setError(String errorType) {
        this.error = errorType;
    }

    /**
     * Gets the detailed error message.
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the detailed error message.
     *
     * @param errorMessage the error message
     */
    public void setMessage(String errorMessage) {
        this.message = errorMessage;
    }

    /**
     * Gets the HTTP status code.
     *
     * @return the status code
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the HTTP status code.
     *
     * @param statusCode the status code
     */
    public void setStatus(int statusCode) {
        this.status = statusCode;
    }

    /**
     * Gets the request path.
     *
     * @return the request path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the request path.
     *
     * @param requestPath the request path
     */
    public void setPath(String requestPath) {
        this.path = requestPath;
    }

    /**
     * Gets the timestamp when the error occurred.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp when the error occurred.
     *
     * @param errorTimestamp the timestamp
     */
    public void setTimestamp(LocalDateTime errorTimestamp) {
        this.timestamp = errorTimestamp;
    }
}
