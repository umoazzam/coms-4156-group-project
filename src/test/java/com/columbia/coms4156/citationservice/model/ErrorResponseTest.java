package com.columbia.coms4156.citationservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ErrorResponse class.
 */
class ErrorResponseTest {

    private ErrorResponse errorResponse;

    @BeforeEach
    void setUp() {
        errorResponse = new ErrorResponse();
    }

    @Test
    void testDefaultConstructor() {
        ErrorResponse response = new ErrorResponse();
        assertNotNull(response);
        assertNotNull(response.getTimestamp());
        assertNull(response.getError());
        assertNull(response.getMessage());
        assertEquals(0, response.getStatus());
        assertNull(response.getPath());
    }

    @Test
    void testParameterizedConstructor() {
        String errorType = "Validation Error";
        String message = "Invalid input";
        int status = 400;
        String path = "/api/cite/book/1";

        ErrorResponse response = new ErrorResponse(errorType, message, status, path);

        assertNotNull(response);
        assertEquals(errorType, response.getError());
        assertEquals(message, response.getMessage());
        assertEquals(status, response.getStatus());
        assertEquals(path, response.getPath());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void testGetError() {
        errorResponse.setError("Invalid Request");
        assertEquals("Invalid Request", errorResponse.getError());
    }

    @Test
    void testSetError() {
        errorResponse.setError("Not Found");
        assertEquals("Not Found", errorResponse.getError());
    }

    @Test
    void testSetErrorNull() {
        errorResponse.setError(null);
        assertNull(errorResponse.getError());
    }

    @Test
    void testSetErrorEmpty() {
        errorResponse.setError("");
        assertEquals("", errorResponse.getError());
    }

    @Test
    void testGetMessage() {
        errorResponse.setMessage("Resource not found");
        assertEquals("Resource not found", errorResponse.getMessage());
    }

    @Test
    void testSetMessage() {
        errorResponse.setMessage("Internal server error");
        assertEquals("Internal server error", errorResponse.getMessage());
    }

    @Test
    void testSetMessageNull() {
        errorResponse.setMessage(null);
        assertNull(errorResponse.getMessage());
    }

    @Test
    void testGetStatus() {
        errorResponse.setStatus(404);
        assertEquals(404, errorResponse.getStatus());
    }

    @Test
    void testSetStatus() {
        errorResponse.setStatus(500);
        assertEquals(500, errorResponse.getStatus());
    }

    @Test
    void testSetStatusBadRequest() {
        errorResponse.setStatus(400);
        assertEquals(400, errorResponse.getStatus());
    }

    @Test
    void testSetStatusUnauthorized() {
        errorResponse.setStatus(401);
        assertEquals(401, errorResponse.getStatus());
    }

    @Test
    void testSetStatusForbidden() {
        errorResponse.setStatus(403);
        assertEquals(403, errorResponse.getStatus());
    }

    @Test
    void testGetPath() {
        errorResponse.setPath("/api/cite/video/123");
        assertEquals("/api/cite/video/123", errorResponse.getPath());
    }

    @Test
    void testSetPath() {
        errorResponse.setPath("/api/source/article");
        assertEquals("/api/source/article", errorResponse.getPath());
    }

    @Test
    void testSetPathNull() {
        errorResponse.setPath(null);
        assertNull(errorResponse.getPath());
    }

    @Test
    void testGetTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        errorResponse.setTimestamp(timestamp);
        assertEquals(timestamp, errorResponse.getTimestamp());
    }

    @Test
    void testSetTimestamp() {
        LocalDateTime timestamp = LocalDateTime.of(2023, 1, 15, 10, 30);
        errorResponse.setTimestamp(timestamp);
        assertEquals(timestamp, errorResponse.getTimestamp());
    }

    @Test
    void testSetTimestampNull() {
        errorResponse.setTimestamp(null);
        assertNull(errorResponse.getTimestamp());
    }

    @Test
    void testCompleteErrorResponseSetup() {
        ErrorResponse complete = new ErrorResponse(
            "Unauthorized",
            "Authentication required to access this resource",
            401,
            "/api/source/book/1"
        );

        assertEquals("Unauthorized", complete.getError());
        assertEquals("Authentication required to access this resource", complete.getMessage());
        assertEquals(401, complete.getStatus());
        assertEquals("/api/source/book/1", complete.getPath());
        assertNotNull(complete.getTimestamp());
    }

    @Test
    void testErrorResponseWithAllFields() {
        ErrorResponse response = new ErrorResponse();
        response.setError("Test Error");
        response.setMessage("Test message");
        response.setStatus(500);
        response.setPath("/test/path");
        LocalDateTime testTime = LocalDateTime.now();
        response.setTimestamp(testTime);

        assertEquals("Test Error", response.getError());
        assertEquals("Test message", response.getMessage());
        assertEquals(500, response.getStatus());
        assertEquals("/test/path", response.getPath());
        assertEquals(testTime, response.getTimestamp());
    }

    @Test
    void testMultipleErrorResponsesHaveDifferentTimestamps() throws InterruptedException {
        ErrorResponse response1 = new ErrorResponse();
        Thread.sleep(1);
        ErrorResponse response2 = new ErrorResponse();

        assertNotEquals(response1.getTimestamp(), response2.getTimestamp());
    }
}

