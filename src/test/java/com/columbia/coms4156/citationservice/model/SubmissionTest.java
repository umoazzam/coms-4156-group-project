package com.columbia.coms4156.citationservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the Submission class.
 * Tests all setter methods, citation management, and their parameter validation.
 */
class SubmissionTest {

    private Submission submission;
    private User testUser;
    private Citation testCitation;

    @BeforeEach
    void setUp() {
        submission = new Submission();
        testUser = new User();
        testUser.setId(1L);
        testCitation = new Citation();
    }

    @Test
    void testDefaultConstructor() {
        Submission newSubmission = new Submission();
        assertNotNull(newSubmission);
        assertNull(newSubmission.getId());
        assertNull(newSubmission.getUser());
        assertNotNull(newSubmission.getDate());
        assertNull(newSubmission.getFormat());
        assertNotNull(newSubmission.getCitations());
        assertTrue(newSubmission.getCitations().isEmpty());

        // Date should be set to current time (within a few seconds)
        assertTrue(newSubmission.getDate().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(newSubmission.getDate().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void testParameterizedConstructor() {
        User user = new User();
        String format = "MLA";
        Submission newSubmission = new Submission(user, format);

        assertNotNull(newSubmission);
        assertEquals(user, newSubmission.getUser());
        assertEquals(format, newSubmission.getFormat());
        assertNotNull(newSubmission.getDate());
        assertNotNull(newSubmission.getCitations());
        assertTrue(newSubmission.getCitations().isEmpty());
    }

    // User setter tests
    @Test
    void testSetUserValid() {
        User validUser = new User();
        validUser.setId(2L);
        submission.setUser(validUser);
        assertEquals(validUser, submission.getUser());
    }

    // Date setter tests
    @Test
    void testSetDateValid() {
        LocalDateTime pastDate = LocalDateTime.now().minusHours(1);
        submission.setDate(pastDate);
        assertEquals(pastDate, submission.getDate());
    }

    @Test
    void testSetDateCurrentTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        submission.setDate(currentTime);
        assertEquals(currentTime, submission.getDate());
    }

    @Test
    void testSetDateNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> submission.setDate(null)
        );
        assertEquals("Date cannot be null", exception.getMessage());
    }

    @Test
    void testSetDateInFuture() {
        LocalDateTime futureDate = LocalDateTime.now().plusHours(1);
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> submission.setDate(futureDate)
        );
        assertEquals("Date cannot be in the future", exception.getMessage());
    }

    @Test
    void testSetDateFarInFuture() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> submission.setDate(futureDate)
        );
        assertEquals("Date cannot be in the future", exception.getMessage());
    }

    // Format setter tests - Valid cases
    @Test
    void testSetFormatMLA() {
        submission.setFormat("MLA");
        assertEquals("MLA", submission.getFormat());
    }

    @Test
    void testSetFormatAPA() {
        submission.setFormat("APA");
        assertEquals("APA", submission.getFormat());
    }

    @Test
    void testSetFormatChicago() {
        submission.setFormat("Chicago");
        assertEquals("Chicago", submission.getFormat());
    }

    @Test
    void testSetFormatCaseInsensitive() {
        submission.setFormat("mla");
        assertEquals("mla", submission.getFormat());

        submission.setFormat("apa");
        assertEquals("apa", submission.getFormat());

        submission.setFormat("chicago");
        assertEquals("chicago", submission.getFormat());

        submission.setFormat("MlA");
        assertEquals("MlA", submission.getFormat());
    }

    @Test
    void testSetFormatWithWhitespace() {
        submission.setFormat("  MLA  ");
        assertEquals("  MLA  ", submission.getFormat());
    }

    // Format setter tests - Invalid cases
    @Test
    void testSetFormatNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> submission.setFormat(null)
        );
        assertEquals("Format cannot be null", exception.getMessage());
    }

    @Test
    void testSetFormatBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> submission.setFormat("   ")
        );
        assertEquals("Format cannot be blank", exception.getMessage());
    }

    @Test
    void testSetFormatEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> submission.setFormat("")
        );
        assertEquals("Format cannot be blank", exception.getMessage());
    }

    @Test
    void testSetFormatInvalid() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> submission.setFormat("Harvard")
        );
        assertEquals("Format must be one of: MLA, APA, Chicago", exception.getMessage());
    }

    @Test
    void testSetFormatInvalidIEEE() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> submission.setFormat("IEEE")
        );
        assertEquals("Format must be one of: MLA, APA, Chicago", exception.getMessage());
    }

    @Test
    void testSetFormatInvalidRandom() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> submission.setFormat("RandomFormat")
        );
        assertEquals("Format must be one of: MLA, APA, Chicago", exception.getMessage());
    }

    // Citations setter tests
    @Test
    void testSetCitationsValid() {
        List<Citation> citations = new ArrayList<>();
        citations.add(new Citation());
        submission.setCitations(citations);
        assertEquals(citations, submission.getCitations());
    }

    @Test
    void testSetCitationsEmptyList() {
        List<Citation> emptyList = new ArrayList<>();
        submission.setCitations(emptyList);
        assertEquals(emptyList, submission.getCitations());
        assertTrue(submission.getCitations().isEmpty());
    }

    @Test
    void testSetCitationsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> submission.setCitations(null)
        );
        assertEquals("Citations list cannot be null", exception.getMessage());
    }

    // AddCitation method tests
    @Test
    void testAddCitationValid() {
        Citation citation = new Citation();
        submission.addCitation(citation);
        assertTrue(submission.getCitations().contains(citation));
        assertEquals(submission, citation.getSubmission());
    }

    @Test
    void testAddCitationAlreadyAdded() {
        Citation citation = new Citation();
        submission.addCitation(citation);
        submission.addCitation(citation); // Add again

        // Should only appear once in the list
        assertEquals(1, submission.getCitations().size());
        assertTrue(submission.getCitations().contains(citation));
        assertEquals(submission, citation.getSubmission());
    }

    @Test
    void testAddCitationNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> submission.addCitation(null)
        );
        assertEquals("Citation cannot be null", exception.getMessage());
    }

    @Test
    void testAddCitationBelongsToAnotherSubmission() {
        Submission anotherSubmission = new Submission();
        Citation citation = new Citation();
        citation.setSubmission(anotherSubmission);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> submission.addCitation(citation)
        );
        assertEquals("Citation already belongs to another submission", exception.getMessage());
    }

    // RemoveCitation method tests
    @Test
    void testRemoveCitationValid() {
        Citation citation = new Citation();
        submission.addCitation(citation);

        submission.removeCitation(citation);
        assertFalse(submission.getCitations().contains(citation));
        assertNull(citation.getSubmission());
    }

    @Test
    void testRemoveCitationNotInList() {
        Citation citation = new Citation();
        // Don't add the citation to submission's list

        submission.removeCitation(citation);
        // Should not cause any issues
        assertFalse(submission.getCitations().contains(citation));
    }

    @Test
    void testRemoveCitationNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> submission.removeCitation(null)
        );
        assertEquals("Citation cannot be null", exception.getMessage());
    }

    // ToString method test
    @Test
    void testToString() {
        submission.setId(1L);
        submission.setUser(testUser);
        submission.setFormat("MLA");
        submission.addCitation(new Citation());
        submission.addCitation(new Citation());

        String result = submission.toString();
        assertNotNull(result);
        assertTrue(result.contains("Submission{"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("userId=1"));
        assertTrue(result.contains("format='MLA'"));
        assertTrue(result.contains("citationsCount=2"));
    }

    @Test
    void testToStringWithNullUser() {
        submission.setId(2L);
        submission.setFormat("APA");

        String result = submission.toString();
        assertNotNull(result);
        assertTrue(result.contains("Submission{"));
        assertTrue(result.contains("id=2"));
        assertTrue(result.contains("userId=null"));
        assertTrue(result.contains("format='APA'"));
        assertTrue(result.contains("citationsCount=0"));
    }

    // Integration tests
    @Test
    void testCompleteSubmissionSetup() {
        User user = new User();
        user.setId(5L);

        Submission completeSubmission = new Submission(user, "Chicago");
        completeSubmission.setId(10L);

        Citation citation1 = new Citation();
        Citation citation2 = new Citation();
        Citation citation3 = new Citation();

        completeSubmission.addCitation(citation1);
        completeSubmission.addCitation(citation2);
        completeSubmission.addCitation(citation3);

        assertEquals(user, completeSubmission.getUser());
        assertEquals("Chicago", completeSubmission.getFormat());
        assertEquals(Long.valueOf(10L), completeSubmission.getId());
        assertEquals(3, completeSubmission.getCitations().size());
        assertTrue(completeSubmission.getCitations().contains(citation1));
        assertTrue(completeSubmission.getCitations().contains(citation2));
        assertTrue(completeSubmission.getCitations().contains(citation3));
        assertEquals(completeSubmission, citation1.getSubmission());
        assertEquals(completeSubmission, citation2.getSubmission());
        assertEquals(completeSubmission, citation3.getSubmission());
    }

    @Test
    void testCitationManagementWorkflow() {
        submission.setUser(testUser);
        submission.setFormat("APA");

        Citation cit1 = new Citation();
        Citation cit2 = new Citation();
        Citation cit3 = new Citation();

        // Add citations
        submission.addCitation(cit1);
        submission.addCitation(cit2);
        submission.addCitation(cit3);
        assertEquals(3, submission.getCitations().size());

        // Remove one citation
        submission.removeCitation(cit2);
        assertEquals(2, submission.getCitations().size());
        assertFalse(submission.getCitations().contains(cit2));
        assertNull(cit2.getSubmission());

        // Verify remaining citations
        assertTrue(submission.getCitations().contains(cit1));
        assertTrue(submission.getCitations().contains(cit3));
        assertEquals(submission, cit1.getSubmission());
        assertEquals(submission, cit3.getSubmission());
    }

    // Edge case tests
    @Test
    void testMultipleFormatValidations() {
        String[] validFormats = {"MLA", "APA", "Chicago", "mla", "apa", "chicago", "MlA", "ApA", "CHICAGO"};

        for (String format : validFormats) {
            assertDoesNotThrow(() -> submission.setFormat(format),
                "Failed for format: " + format);
            assertEquals(format, submission.getFormat());
        }
    }

    @Test
    void testMultipleInvalidFormats() {
        String[] invalidFormats = {"Harvard", "IEEE", "Vancouver", "APSA", "Turabian", "CSE", "ASA"};

        for (String format : invalidFormats) {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> submission.setFormat(format),
                "Should have failed for format: " + format
            );
            assertEquals("Format must be one of: MLA, APA, Chicago", exception.getMessage());
        }
    }

    @Test
    void testDatePrecision() {
        LocalDateTime precisePastTime = LocalDateTime.now().minusNanos(1000000); // 1 millisecond ago
        assertDoesNotThrow(() -> submission.setDate(precisePastTime));
        assertEquals(precisePastTime, submission.getDate());
    }

    @Test
    void testCitationOwnershipTransfer() {
        Submission submission1 = new Submission();
        Submission submission2 = new Submission();
        Citation citation = new Citation();

        // Add citation to first submission
        submission1.addCitation(citation);
        assertEquals(submission1, citation.getSubmission());
        assertTrue(submission1.getCitations().contains(citation));

        // Remove from first submission
        submission1.removeCitation(citation);
        assertNull(citation.getSubmission());
        assertFalse(submission1.getCitations().contains(citation));

        // Add to second submission
        submission2.addCitation(citation);
        assertEquals(submission2, citation.getSubmission());
        assertTrue(submission2.getCitations().contains(citation));
    }

    @Test
    void testConstructorSetsCurrentDate() {
        LocalDateTime beforeCreation = LocalDateTime.now().minusSeconds(1);
        Submission newSubmission = new Submission();
        LocalDateTime afterCreation = LocalDateTime.now().plusSeconds(1);

        assertTrue(newSubmission.getDate().isAfter(beforeCreation));
        assertTrue(newSubmission.getDate().isBefore(afterCreation));
    }

    @Test
    void testParameterizedConstructorSetsCurrentDate() {
        User user = new User();
        LocalDateTime beforeCreation = LocalDateTime.now().minusSeconds(1);
        Submission newSubmission = new Submission(user, "MLA");
        LocalDateTime afterCreation = LocalDateTime.now().plusSeconds(1);

        assertTrue(newSubmission.getDate().isAfter(beforeCreation));
        assertTrue(newSubmission.getDate().isBefore(afterCreation));
    }
}
