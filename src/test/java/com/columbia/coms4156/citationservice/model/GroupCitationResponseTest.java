package com.columbia.coms4156.citationservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit tests for the GroupCitationResponse class.
 * Tests all setter methods and their parameter validation.
 */
class GroupCitationResponseTest {

    private GroupCitationResponse groupResponse;
    private Map<String, String> testCitations;

    @BeforeEach
    void setUp() {
        groupResponse = new GroupCitationResponse();
        testCitations = new HashMap<>();
        testCitations.put("citation_1", "Author, A. (2023). Title 1. Publisher.");
        testCitations.put("citation_2", "Author, B. (2023). Title 2. Publisher.");
    }

    @Test
    void testDefaultConstructor() {
        GroupCitationResponse newResponse = new GroupCitationResponse();
        assertNotNull(newResponse);
        assertNull(newResponse.getSubmissionId());
        assertNull(newResponse.getCitations());
    }

    @Test
    void testParameterizedConstructor() {
        Long submissionId = 123L;

        GroupCitationResponse newResponse = new GroupCitationResponse(submissionId, testCitations);

        assertNotNull(newResponse);
        assertEquals(submissionId, newResponse.getSubmissionId());
        assertEquals(testCitations, newResponse.getCitations());
    }

    // Submission ID setter tests
    @Test
    void testSetSubmissionIdValid() {
        Long validId = 456L;
        groupResponse.setSubmissionId(validId);
        assertEquals(validId, groupResponse.getSubmissionId());
    }

    @Test
    void testSetSubmissionIdZero() {
        Long zeroId = 0L;
        groupResponse.setSubmissionId(zeroId);
        assertEquals(zeroId, groupResponse.getSubmissionId());
    }

    @Test
    void testSetSubmissionIdNull() {
        assertDoesNotThrow(() -> groupResponse.setSubmissionId(null));
        assertNull(groupResponse.getSubmissionId());
    }

    @Test
    void testSetSubmissionIdNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> groupResponse.setSubmissionId(-1L)
        );
        assertEquals("Submission ID cannot be negative", exception.getMessage());
    }

    @Test
    void testSetSubmissionIdLargeNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> groupResponse.setSubmissionId(-999L)
        );
        assertEquals("Submission ID cannot be negative", exception.getMessage());
    }

    // Citations map setter tests
    @Test
    void testSetCitationsValid() {
        groupResponse.setCitations(testCitations);
        assertEquals(testCitations, groupResponse.getCitations());
    }

    @Test
    void testSetCitationsEmptyMap() {
        Map<String, String> emptyMap = new HashMap<>();
        groupResponse.setCitations(emptyMap);
        assertEquals(emptyMap, groupResponse.getCitations());
        assertTrue(groupResponse.getCitations().isEmpty());
    }

    @Test
    void testSetCitationsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> groupResponse.setCitations(null)
        );
        assertEquals("Citations map cannot be null", exception.getMessage());
    }

    @Test
    void testSetCitationsLargeMap() {
        Map<String, String> largeMap = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            largeMap.put("citation_" + i, "Citation string " + i);
        }

        groupResponse.setCitations(largeMap);
        assertEquals(largeMap, groupResponse.getCitations());
        assertEquals(100, groupResponse.getCitations().size());
    }

    // ToString test
    @Test
    void testToString() {
        groupResponse.setSubmissionId(789L);
        groupResponse.setCitations(testCitations);

        String result = groupResponse.toString();
        assertNotNull(result);
        assertTrue(result.contains("GroupCitationResponse{"));
        assertTrue(result.contains("submissionId=789"));
        assertTrue(result.contains("citations="));
    }

    @Test
    void testToStringWithNullValues() {
        String result = groupResponse.toString();
        assertNotNull(result);
        assertTrue(result.contains("GroupCitationResponse{"));
        assertTrue(result.contains("submissionId=null"));
        assertTrue(result.contains("citations=null"));
    }

    // Integration tests
    @Test
    void testCompleteSetup() {
        Long submissionId = 999L;
        Map<String, String> citations = new HashMap<>();
        citations.put("cit1", "Citation 1");
        citations.put("cit2", "Citation 2");
        citations.put("cit3", "Citation 3");

        groupResponse.setSubmissionId(submissionId);
        groupResponse.setCitations(citations);

        assertEquals(submissionId, groupResponse.getSubmissionId());
        assertEquals(citations, groupResponse.getCitations());
        assertEquals(3, groupResponse.getCitations().size());
    }

    @Test
    void testMultipleUpdates() {
        // Test updating values multiple times
        Map<String, String> firstMap = new HashMap<>();
        firstMap.put("id1", "citation1");

        groupResponse.setSubmissionId(100L);
        groupResponse.setCitations(firstMap);

        assertEquals(Long.valueOf(100L), groupResponse.getSubmissionId());
        assertEquals(firstMap, groupResponse.getCitations());

        Map<String, String> secondMap = new HashMap<>();
        secondMap.put("id2", "citation2");
        secondMap.put("id3", "citation3");

        groupResponse.setSubmissionId(200L);
        groupResponse.setCitations(secondMap);

        assertEquals(Long.valueOf(200L), groupResponse.getSubmissionId());
        assertEquals(secondMap, groupResponse.getCitations());
        assertEquals(2, groupResponse.getCitations().size());
    }

    @Test
    void testCitationsMapWithVariousFormats() {
        Map<String, String> variousFormats = new HashMap<>();
        variousFormats.put("apa_1", "Author, A. (2023). APA Format. Publisher.");
        variousFormats.put("mla_1", "Author, First. \"MLA Format.\" Journal, 2023.");
        variousFormats.put("chicago_1", "Author, First. \"Chicago Format.\" Journal (2023).");

        assertDoesNotThrow(() -> groupResponse.setCitations(variousFormats));
        assertEquals(variousFormats, groupResponse.getCitations());
        assertEquals(3, groupResponse.getCitations().size());
    }

    @Test
    void testCitationsMapWithSpecialCharacters() {
        Map<String, String> specialChars = new HashMap<>();
        specialChars.put("unicode_1", "García, J. (2023). Título en Español. Editorial.");
        specialChars.put("symbols_1", "O'Connor, M. & Smith-Jones, A. (2023). \"Complex: Title!\" Journal.");

        assertDoesNotThrow(() -> groupResponse.setCitations(specialChars));
        assertEquals(specialChars, groupResponse.getCitations());
    }

    @Test
    void testSubmissionIdBoundaryValues() {
        // Test with maximum Long value
        Long maxValue = Long.MAX_VALUE;
        assertDoesNotThrow(() -> groupResponse.setSubmissionId(maxValue));
        assertEquals(maxValue, groupResponse.getSubmissionId());

        // Test with 1 (minimum positive value)
        Long minPositive = 1L;
        assertDoesNotThrow(() -> groupResponse.setSubmissionId(minPositive));
        assertEquals(minPositive, groupResponse.getSubmissionId());
    }

    @Test
    void testCitationsMapModification() {
        Map<String, String> originalMap = new HashMap<>();
        originalMap.put("test_id", "test_citation");

        groupResponse.setCitations(originalMap);
        Map<String, String> retrievedMap = groupResponse.getCitations();

        // Verify the map was set correctly
        assertEquals(originalMap, retrievedMap);
        assertEquals("test_citation", retrievedMap.get("test_id"));
    }
}
