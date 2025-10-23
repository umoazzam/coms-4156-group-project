package com.columbia.coms4156.citationservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Citation class.
 * Tests all setter methods and their parameter validation.
 */
class CitationTest {

    private Citation citation;
    private Submission testSubmission;

    @BeforeEach
    void setUp() {
        citation = new Citation();
        testSubmission = new Submission();
        testSubmission.setId(1L);
    }

    @Test
    void testDefaultConstructor() {
        Citation newCitation = new Citation();
        assertNotNull(newCitation);
        assertNull(newCitation.getId());
        assertNull(newCitation.getSubmission());
        assertNull(newCitation.getUserInputMetaData());
        assertNull(newCitation.getMediaId());
        assertNull(newCitation.getMediaType());
    }

    @Test
    void testParameterizedConstructor() {
        String metadata = "{'title': 'Test Book'}";
        Long mediaId = 123L;
        String mediaType = "book";

        Citation newCitation = new Citation(testSubmission, metadata, mediaId, mediaType);

        assertNotNull(newCitation);
        assertEquals(testSubmission, newCitation.getSubmission());
        assertEquals(metadata, newCitation.getUserInputMetaData());
        assertEquals(mediaId, newCitation.getMediaId());
        assertEquals(mediaType, newCitation.getMediaType());
    }

    // ID setter tests
    @Test
    void testSetIdValid() {
        Long validId = 42L;
        citation.setId(validId);
        assertEquals(validId, citation.getId());
    }

    @Test
    void testSetIdZero() {
        Long zeroId = 0L;
        citation.setId(zeroId);
        assertEquals(zeroId, citation.getId());
    }

    @Test
    void testSetIdNull() {
        assertDoesNotThrow(() -> citation.setId(null));
        assertNull(citation.getId());
    }

    @Test
    void testSetIdNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citation.setId(-1L)
        );
        assertEquals("ID cannot be negative", exception.getMessage());
    }

    // Submission setter tests
    @Test
    void testSetSubmissionValid() {
        citation.setSubmission(testSubmission);
        assertEquals(testSubmission, citation.getSubmission());
    }

    // User input metadata setter tests
    @Test
    void testSetUserInputMetaDataValid() {
        String validMetadata = "{'title': 'Valid JSON'}";
        citation.setUserInputMetaData(validMetadata);
        assertEquals(validMetadata, citation.getUserInputMetaData());
    }

    @Test
    void testSetUserInputMetaDataNull() {
        assertDoesNotThrow(() -> citation.setUserInputMetaData(null));
        assertNull(citation.getUserInputMetaData());
    }

    @Test
    void testSetUserInputMetaDataBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citation.setUserInputMetaData("   ")
        );
        assertEquals("User input metadata cannot be blank", exception.getMessage());
    }

    @Test
    void testSetUserInputMetaDataEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citation.setUserInputMetaData("")
        );
        assertEquals("User input metadata cannot be blank", exception.getMessage());
    }

    // Media ID setter tests
    @Test
    void testSetMediaIdValid() {
        Long validMediaId = 100L;
        citation.setMediaId(validMediaId);
        assertEquals(validMediaId, citation.getMediaId());
    }

    @Test
    void testSetMediaIdNull() {
        assertDoesNotThrow(() -> citation.setMediaId(null));
        assertNull(citation.getMediaId());
    }

    @Test
    void testSetMediaIdNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citation.setMediaId(-5L)
        );
        assertEquals("Media ID cannot be negative", exception.getMessage());
    }

    // Media type setter tests
    @Test
    void testSetMediaTypeValid() {
        String validType = "article";
        citation.setMediaType(validType);
        assertEquals(validType, citation.getMediaType());
    }

    @Test
    void testSetMediaTypeNull() {
        assertDoesNotThrow(() -> citation.setMediaType(null));
        assertNull(citation.getMediaType());
    }

    @Test
    void testSetMediaTypeBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citation.setMediaType("   ")
        );
        assertEquals("Media type cannot be blank", exception.getMessage());
    }

    @Test
    void testSetMediaTypeEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citation.setMediaType("")
        );
        assertEquals("Media type cannot be blank", exception.getMessage());
    }

    // ToString test
    @Test
    void testToString() {
        citation.setId(1L);
        citation.setSubmission(testSubmission);
        citation.setMediaId(123L);
        citation.setMediaType("book");

        String result = citation.toString();
        assertNotNull(result);
        assertTrue(result.contains("Citation{"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("submissionId=1"));
        assertTrue(result.contains("mediaId=123"));
        assertTrue(result.contains("mediaType='book'"));
    }

    // Integration test
    @Test
    void testCompleteCitationSetup() {
        citation.setId(10L);
        citation.setSubmission(testSubmission);
        citation.setUserInputMetaData("{'title': 'Complete Test'}");
        citation.setMediaId(456L);
        citation.setMediaType("video");

        assertEquals(Long.valueOf(10L), citation.getId());
        assertEquals(testSubmission, citation.getSubmission());
        assertEquals("{'title': 'Complete Test'}", citation.getUserInputMetaData());
        assertEquals(Long.valueOf(456L), citation.getMediaId());
        assertEquals("video", citation.getMediaType());
    }

    @Test
    void testValidMediaTypes() {
        String[] validTypes = {"book", "article", "video", "webpage"};

        for (String type : validTypes) {
            assertDoesNotThrow(() -> citation.setMediaType(type));
            assertEquals(type, citation.getMediaType());
        }
    }
}
