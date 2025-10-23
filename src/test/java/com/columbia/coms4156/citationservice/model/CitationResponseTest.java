package com.columbia.coms4156.citationservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CitationResponse class.
 * Tests all setter methods and their parameter validation.
 */
class CitationResponseTest {

    private CitationResponse citationResponse;

    @BeforeEach
    void setUp() {
        citationResponse = new CitationResponse();
    }

    @Test
    void testDefaultConstructor() {
        CitationResponse newResponse = new CitationResponse();
        assertNotNull(newResponse);
        assertNull(newResponse.getCitationId());
        assertNull(newResponse.getCitationString());
    }

    @Test
    void testParameterizedConstructor() {
        String citationId = "citation_123";
        String citationString = "Doe, J. (2023). Sample Article. Journal of Examples, 1(1), 1-10.";

        CitationResponse newResponse = new CitationResponse(citationId, citationString);

        assertNotNull(newResponse);
        assertEquals(citationId, newResponse.getCitationId());
        assertEquals(citationString, newResponse.getCitationString());
    }

    // Citation ID setter tests
    @Test
    void testSetCitationIdValid() {
        String validId = "citation_456";
        citationResponse.setCitationId(validId);
        assertEquals(validId, citationResponse.getCitationId());
    }

    @Test
    void testSetCitationIdWithNumbers() {
        String idWithNumbers = "citation_123_test";
        citationResponse.setCitationId(idWithNumbers);
        assertEquals(idWithNumbers, citationResponse.getCitationId());
    }

    @Test
    void testSetCitationIdNull() {
        assertDoesNotThrow(() -> citationResponse.setCitationId(null));
        assertNull(citationResponse.getCitationId());
    }

    @Test
    void testSetCitationIdBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citationResponse.setCitationId("   ")
        );
        assertEquals("Citation ID cannot be blank", exception.getMessage());
    }

    @Test
    void testSetCitationIdEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citationResponse.setCitationId("")
        );
        assertEquals("Citation ID cannot be blank", exception.getMessage());
    }

    // Citation string setter tests
    @Test
    void testSetCitationStringValid() {
        String validString = "Smith, A. (2023). Research Methods. Academic Press.";
        citationResponse.setCitationString(validString);
        assertEquals(validString, citationResponse.getCitationString());
    }

    @Test
    void testSetCitationStringLong() {
        String longString = "This is a very long citation string that contains multiple authors, " +
                           "a very long title, and extensive publication information that might " +
                           "span multiple lines in a typical bibliography format.";
        citationResponse.setCitationString(longString);
        assertEquals(longString, citationResponse.getCitationString());
    }

    @Test
    void testSetCitationStringWithSpecialCharacters() {
        String stringWithSpecial = "García, J. & O'Connor, M. (2023). \"Complex Title: A Study.\" Journal, 1(1), 1-10.";
        citationResponse.setCitationString(stringWithSpecial);
        assertEquals(stringWithSpecial, citationResponse.getCitationString());
    }

    @Test
    void testSetCitationStringNull() {
        assertDoesNotThrow(() -> citationResponse.setCitationString(null));
        assertNull(citationResponse.getCitationString());
    }

    @Test
    void testSetCitationStringBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citationResponse.setCitationString("   ")
        );
        assertEquals("Citation string cannot be blank", exception.getMessage());
    }

    @Test
    void testSetCitationStringEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> citationResponse.setCitationString("")
        );
        assertEquals("Citation string cannot be blank", exception.getMessage());
    }

    // ToString test
    @Test
    void testToString() {
        citationResponse.setCitationId("test_id");
        citationResponse.setCitationString("Test Citation String");

        String result = citationResponse.toString();
        assertNotNull(result);
        assertTrue(result.contains("CitationResponse{"));
        assertTrue(result.contains("citationId='test_id'"));
        assertTrue(result.contains("citationString='Test Citation String'"));
    }

    @Test
    void testToStringWithNullValues() {
        String result = citationResponse.toString();
        assertNotNull(result);
        assertTrue(result.contains("CitationResponse{"));
        assertTrue(result.contains("citationId='null'"));
        assertTrue(result.contains("citationString='null'"));
    }

    // Integration tests
    @Test
    void testCompleteSetup() {
        String id = "citation_789";
        String citation = "Brown, L. (2023). Modern Techniques. Science Publishers.";

        citationResponse.setCitationId(id);
        citationResponse.setCitationString(citation);

        assertEquals(id, citationResponse.getCitationId());
        assertEquals(citation, citationResponse.getCitationString());
    }

    @Test
    void testMultipleUpdates() {
        // Test updating values multiple times
        citationResponse.setCitationId("id1");
        citationResponse.setCitationString("citation1");

        assertEquals("id1", citationResponse.getCitationId());
        assertEquals("citation1", citationResponse.getCitationString());

        citationResponse.setCitationId("id2");
        citationResponse.setCitationString("citation2");

        assertEquals("id2", citationResponse.getCitationId());
        assertEquals("citation2", citationResponse.getCitationString());
    }

    @Test
    void testVariousCitationFormats() {
        String[] citationFormats = {
            "APA: Author, A. (2023). Title. Publisher.",
            "MLA: Author, First. \"Title.\" Journal, vol. 1, no. 1, 2023, pp. 1-10.",
            "Chicago: Author, First. \"Title.\" Journal 1, no. 1 (2023): 1-10."
        };

        for (String format : citationFormats) {
            assertDoesNotThrow(() -> citationResponse.setCitationString(format));
            assertEquals(format, citationResponse.getCitationString());
        }
    }
}
