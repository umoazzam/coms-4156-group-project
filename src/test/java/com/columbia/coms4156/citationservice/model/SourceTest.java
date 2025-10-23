package com.columbia.coms4156.citationservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Source class.
 * Tests all setter methods, constructors, and their parameter validation.
 * Uses a concrete test implementation since Source is abstract.
 */
class SourceTest {

    /**
     * Concrete implementation of Source for testing purposes.
     */
    private static class TestSource extends Source {
        public TestSource() {
            super();
        }

        public TestSource(String title, String author) {
            super(title, author);
        }
    }

    private TestSource source;

    @BeforeEach
    void setUp() {
        source = new TestSource();
    }

    @Test
    void testDefaultConstructor() {
        TestSource newSource = new TestSource();
        assertNotNull(newSource);
        assertNull(newSource.getId());
        assertNull(newSource.getTitle());
        assertNull(newSource.getAuthor());
    }

    @Test
    void testParameterizedConstructorValid() {
        String title = "Test Title";
        String author = "Test Author";
        TestSource newSource = new TestSource(title, author);

        assertNotNull(newSource);
        assertEquals(title, newSource.getTitle());
        assertEquals(author, newSource.getAuthor());
        assertNull(newSource.getId());
    }

    @Test
    void testParameterizedConstructorNullTitle() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new TestSource(null, "Test Author")
        );
        assertEquals("Title cannot be null", exception.getMessage());
    }

    @Test
    void testParameterizedConstructorBlankTitle() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new TestSource("   ", "Test Author")
        );
        assertEquals("Title cannot be blank", exception.getMessage());
    }

    @Test
    void testParameterizedConstructorNullAuthor() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new TestSource("Test Title", null)
        );
        assertEquals("Author cannot be null", exception.getMessage());
    }

    @Test
    void testParameterizedConstructorBlankAuthor() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new TestSource("Test Title", "   ")
        );
        assertEquals("Author cannot be blank", exception.getMessage());
    }

    @Test
    void testParameterizedConstructorBothNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new TestSource(null, null)
        );
        assertEquals("Title cannot be null", exception.getMessage());
    }

    // ID setter tests
    @Test
    void testSetIdValid() {
        Long validId = 123L;
        source.setId(validId);
        assertEquals(validId, source.getId());
    }

    @Test
    void testSetIdZero() {
        Long zeroId = 0L;
        source.setId(zeroId);
        assertEquals(zeroId, source.getId());
    }

    @Test
    void testSetIdNull() {
        assertDoesNotThrow(() -> source.setId(null));
        assertNull(source.getId());
    }

    @Test
    void testSetIdNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> source.setId(-1L)
        );
        assertEquals("ID cannot be negative", exception.getMessage());
    }

    @Test
    void testSetIdLargeNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> source.setId(-999L)
        );
        assertEquals("ID cannot be negative", exception.getMessage());
    }

    @Test
    void testSetIdLargePositive() {
        Long largeId = Long.MAX_VALUE;
        assertDoesNotThrow(() -> source.setId(largeId));
        assertEquals(largeId, source.getId());
    }

    // Title setter tests
    @Test
    void testSetTitleValid() {
        String validTitle = "The Great Gatsby";
        source.setTitle(validTitle);
        assertEquals(validTitle, source.getTitle());
    }

    @Test
    void testSetTitleWithSpaces() {
        String titleWithSpaces = "A Title With Multiple Spaces";
        source.setTitle(titleWithSpaces);
        assertEquals(titleWithSpaces, source.getTitle());
    }

    @Test
    void testSetTitleWithSpecialCharacters() {
        String titleWithSpecialChars = "Title: A Study of Something (2023)";
        source.setTitle(titleWithSpecialChars);
        assertEquals(titleWithSpecialChars, source.getTitle());
    }

    @Test
    void testSetTitleSingleCharacter() {
        String singleChar = "A";
        source.setTitle(singleChar);
        assertEquals(singleChar, source.getTitle());
    }

    @Test
    void testSetTitleVeryLong() {
        String longTitle = "A".repeat(1000);
        source.setTitle(longTitle);
        assertEquals(longTitle, source.getTitle());
    }

    @Test
    void testSetTitleNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> source.setTitle(null)
        );
        assertEquals("Title cannot be null", exception.getMessage());
    }

    @Test
    void testSetTitleEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> source.setTitle("")
        );
        assertEquals("Title cannot be blank", exception.getMessage());
    }

    @Test
    void testSetTitleBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> source.setTitle("   ")
        );
        assertEquals("Title cannot be blank", exception.getMessage());
    }

    @Test
    void testSetTitleOnlyWhitespace() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> source.setTitle("\t\n\r ")
        );
        assertEquals("Title cannot be blank", exception.getMessage());
    }

    // Author setter tests
    @Test
    void testSetAuthorValid() {
        String validAuthor = "F. Scott Fitzgerald";
        source.setAuthor(validAuthor);
        assertEquals(validAuthor, source.getAuthor());
    }

    @Test
    void testSetAuthorWithMiddleName() {
        String authorWithMiddle = "John F. Kennedy";
        source.setAuthor(authorWithMiddle);
        assertEquals(authorWithMiddle, source.getAuthor());
    }

    @Test
    void testSetAuthorMultipleAuthors() {
        String multipleAuthors = "Smith, J. & Doe, J.";
        source.setAuthor(multipleAuthors);
        assertEquals(multipleAuthors, source.getAuthor());
    }

    @Test
    void testSetAuthorWithSuffix() {
        String authorWithSuffix = "Martin Luther King Jr.";
        source.setAuthor(authorWithSuffix);
        assertEquals(authorWithSuffix, source.getAuthor());
    }

    @Test
    void testSetAuthorSingleName() {
        String singleName = "Plato";
        source.setAuthor(singleName);
        assertEquals(singleName, source.getAuthor());
    }

    @Test
    void testSetAuthorNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> source.setAuthor(null)
        );
        assertEquals("Author cannot be null", exception.getMessage());
    }

    @Test
    void testSetAuthorEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> source.setAuthor("")
        );
        assertEquals("Author cannot be blank", exception.getMessage());
    }

    @Test
    void testSetAuthorBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> source.setAuthor("   ")
        );
        assertEquals("Author cannot be blank", exception.getMessage());
    }

    @Test
    void testSetAuthorOnlyWhitespace() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> source.setAuthor("\t\n\r ")
        );
        assertEquals("Author cannot be blank", exception.getMessage());
    }

    // ToString method test
    @Test
    void testToString() {
        source.setId(42L);
        source.setTitle("Test Title");
        source.setAuthor("Test Author");

        String result = source.toString();
        assertNotNull(result);
        assertTrue(result.contains("Media{"));
        assertTrue(result.contains("id=42"));
        assertTrue(result.contains("title='Test Title'"));
        assertTrue(result.contains("author='Test Author'"));
    }

    @Test
    void testToStringWithNullValues() {
        String result = source.toString();
        assertNotNull(result);
        assertTrue(result.contains("Media{"));
        assertTrue(result.contains("id=null"));
        assertTrue(result.contains("title='null'"));
        assertTrue(result.contains("author='null'"));
    }

    // Integration tests
    @Test
    void testCompleteSourceSetup() {
        String title = "To Kill a Mockingbird";
        String author = "Harper Lee";
        Long id = 100L;

        TestSource completeSource = new TestSource(title, author);
        completeSource.setId(id);

        assertEquals(title, completeSource.getTitle());
        assertEquals(author, completeSource.getAuthor());
        assertEquals(id, completeSource.getId());
    }

    @Test
    void testSourceFieldUpdates() {
        // Initial setup
        source.setTitle("Initial Title");
        source.setAuthor("Initial Author");
        source.setId(1L);

        // Verify initial state
        assertEquals("Initial Title", source.getTitle());
        assertEquals("Initial Author", source.getAuthor());
        assertEquals(Long.valueOf(1L), source.getId());

        // Update fields
        source.setTitle("Updated Title");
        source.setAuthor("Updated Author");
        source.setId(2L);

        // Verify updated state
        assertEquals("Updated Title", source.getTitle());
        assertEquals("Updated Author", source.getAuthor());
        assertEquals(Long.valueOf(2L), source.getId());
    }

    // Edge case tests
    @Test
    void testTitleWithLeadingTrailingSpaces() {
        String titleWithSpaces = "  Valid Title  ";
        source.setTitle(titleWithSpaces);
        assertEquals(titleWithSpaces, source.getTitle());
    }

    @Test
    void testAuthorWithLeadingTrailingSpaces() {
        String authorWithSpaces = "  Valid Author  ";
        source.setAuthor(authorWithSpaces);
        assertEquals(authorWithSpaces, source.getAuthor());
    }

    @Test
    void testMultipleSetOperations() {
        // Test that we can set values multiple times
        for (int i = 0; i < 5; i++) {
            source.setTitle("Title " + i);
            source.setAuthor("Author " + i);
            source.setId((long) i);

            assertEquals("Title " + i, source.getTitle());
            assertEquals("Author " + i, source.getAuthor());
            assertEquals(Long.valueOf(i), source.getId());
        }
    }

    @Test
    void testConstructorFollowedBySetters() {
        TestSource constructedSource = new TestSource("Constructor Title", "Constructor Author");

        // Verify constructor set values correctly
        assertEquals("Constructor Title", constructedSource.getTitle());
        assertEquals("Constructor Author", constructedSource.getAuthor());

        // Update via setters
        constructedSource.setTitle("Setter Title");
        constructedSource.setAuthor("Setter Author");
        constructedSource.setId(999L);

        // Verify setter updates
        assertEquals("Setter Title", constructedSource.getTitle());
        assertEquals("Setter Author", constructedSource.getAuthor());
        assertEquals(Long.valueOf(999L), constructedSource.getId());
    }

    @Test
    void testValidationConsistencyBetweenConstructorAndSetters() {
        // Test that constructor and setters have consistent validation

        // Null title should fail in both constructor and setter
        assertThrows(IllegalArgumentException.class, () -> new TestSource(null, "Author"));
        assertThrows(IllegalArgumentException.class, () -> source.setTitle(null));

        // Blank title should fail in both constructor and setter
        assertThrows(IllegalArgumentException.class, () -> new TestSource("  ", "Author"));
        assertThrows(IllegalArgumentException.class, () -> source.setTitle("  "));

        // Null author should fail in both constructor and setter
        assertThrows(IllegalArgumentException.class, () -> new TestSource("Title", null));
        assertThrows(IllegalArgumentException.class, () -> source.setAuthor(null));

        // Blank author should fail in both constructor and setter
        assertThrows(IllegalArgumentException.class, () -> new TestSource("Title", "  "));
        assertThrows(IllegalArgumentException.class, () -> source.setAuthor("  "));
    }

    @Test
    void testSpecialCharactersInTitleAndAuthor() {
        String[] specialTitles = {
            "Title with numbers: 123",
            "Title with punctuation: Hello, World!",
            "Title with symbols: @#$%^&*()",
            "Title with unicode: Café",
            "Title with quotes: \"The Great Work\"",
            "Title with apostrophe: O'Connor's Book"
        };

        String[] specialAuthors = {
            "O'Brien, John",
            "Smith-Jones, Mary",
            "Dr. Jane Doe",
            "Author (Editor)",
            "José García",
            "李明" // Chinese characters
        };

        for (String title : specialTitles) {
            assertDoesNotThrow(() -> source.setTitle(title),
                "Failed for title: " + title);
            assertEquals(title, source.getTitle());
        }

        for (String author : specialAuthors) {
            assertDoesNotThrow(() -> source.setAuthor(author),
                "Failed for author: " + author);
            assertEquals(author, source.getAuthor());
        }
    }
}
