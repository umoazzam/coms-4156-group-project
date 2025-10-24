package com.columbia.coms4156.citationservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Book class.
 * Tests all setter methods and their parameter validation.
 */
class BookTest {

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
    }

    @Test
    void testDefaultConstructor() {
        Book newBook = new Book();
        assertNotNull(newBook);
        assertNull(newBook.getPublisher());
        assertNull(newBook.getPublicationYear());
        assertNull(newBook.getCity());
        assertNull(newBook.getEdition());
        assertNull(newBook.getIsbn());
    }

    @Test
    void testParameterizedConstructor() {
        String title = "Test Book";
        String author = "Test Author";
        Book newBook = new Book(title, author);

        assertNotNull(newBook);
        assertEquals(title, newBook.getTitle());
        assertEquals(author, newBook.getAuthor());
    }

    // Publisher setter tests
    @Test
    void testSetPublisherValid() {
        String validPublisher = "Penguin Random House";
        book.setPublisher(validPublisher);
        assertEquals(validPublisher, book.getPublisher());
    }

    @Test
    void testSetPublisherNull() {
        assertDoesNotThrow(() -> book.setPublisher(null));
        assertNull(book.getPublisher());
    }

    @Test
    void testSetPublisherBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setPublisher("   ")
        );
        assertEquals("Publisher cannot be blank", exception.getMessage());
    }

    @Test
    void testSetPublisherEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setPublisher("")
        );
        assertEquals("Publisher cannot be blank", exception.getMessage());
    }

    // Publication year setter tests
    @Test
    void testSetPublicationYearValid() {
        Integer validYear = 2023;
        book.setPublicationYear(validYear);
        assertEquals(validYear, book.getPublicationYear());
    }

    @Test
    void testSetPublicationYearMinimumValid() {
        Integer minimumYear = 1000;
        assertDoesNotThrow(() -> book.setPublicationYear(minimumYear));
        assertEquals(minimumYear, book.getPublicationYear());
    }

    @Test
    void testSetPublicationYearCurrentYear() {
        Integer currentYear = java.time.Year.now().getValue();
        assertDoesNotThrow(() -> book.setPublicationYear(currentYear));
        assertEquals(currentYear, book.getPublicationYear());
    }

    @Test
    void testSetPublicationYearFutureValid() {
        Integer futureYear = java.time.Year.now().getValue() + 5;
        assertDoesNotThrow(() -> book.setPublicationYear(futureYear));
        assertEquals(futureYear, book.getPublicationYear());
    }

    @Test
    void testSetPublicationYearNull() {
        assertDoesNotThrow(() -> book.setPublicationYear(null));
        assertNull(book.getPublicationYear());
    }

    @Test
    void testSetPublicationYearTooOld() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setPublicationYear(999)
        );
        assertTrue(exception.getMessage().contains("Publication year must be between 1000 and"));
    }

    @Test
    void testSetPublicationYearTooFuture() {
        Integer tooFutureYear = java.time.Year.now().getValue() + 15;
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setPublicationYear(tooFutureYear)
        );
        assertTrue(exception.getMessage().contains("Publication year must be between 1000 and"));
    }

    @Test
    void testSetPublicationYearNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setPublicationYear(-1)
        );
        assertTrue(exception.getMessage().contains("Publication year must be between 1000 and"));
    }

    @Test
    void testSetPublicationYearZero() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setPublicationYear(0)
        );
        assertTrue(exception.getMessage().contains("Publication year must be between 1000 and"));
    }

    // City setter tests
    @Test
    void testSetCityValid() {
        String validCity = "New York";
        book.setCity(validCity);
        assertEquals(validCity, book.getCity());
    }

    @Test
    void testSetCityNull() {
        assertDoesNotThrow(() -> book.setCity(null));
        assertNull(book.getCity());
    }

    @Test
    void testSetCityBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setCity("   ")
        );
        assertEquals("City cannot be blank", exception.getMessage());
    }

    @Test
    void testSetCityEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setCity("")
        );
        assertEquals("City cannot be blank", exception.getMessage());
    }

    // Edition setter tests
    @Test
    void testSetEditionValid() {
        String validEdition = "2nd Edition";
        book.setEdition(validEdition);
        assertEquals(validEdition, book.getEdition());
    }

    @Test
    void testSetEditionNull() {
        assertDoesNotThrow(() -> book.setEdition(null));
        assertNull(book.getEdition());
    }

    @Test
    void testSetEditionBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setEdition("   ")
        );
        assertEquals("Edition cannot be blank", exception.getMessage());
    }

    @Test
    void testSetEditionEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setEdition("")
        );
        assertEquals("Edition cannot be blank", exception.getMessage());
    }

    // ISBN setter tests - Valid cases
    @Test
    void testSetIsbnValidIsbn10() {
        String validIsbn10 = "0140449116"; // The Odyssey - valid checksum
        assertDoesNotThrow(() -> book.setIsbn(validIsbn10));
        assertEquals(validIsbn10, book.getIsbn());
    }

    @Test
    void testSetIsbnValidIsbn10WithHyphens() {
        String validIsbn10 = "0-140-44911-6"; // The Odyssey with hyphens
        assertDoesNotThrow(() -> book.setIsbn(validIsbn10));
        assertEquals(validIsbn10, book.getIsbn());
    }

    @Test
    void testSetIsbnValidIsbn10WithX() {
        String validIsbn10 = "043942089X"; // Valid ISBN-10 with X checksum
        assertDoesNotThrow(() -> book.setIsbn(validIsbn10));
        assertEquals(validIsbn10, book.getIsbn());
    }

    @Test
    void testSetIsbnValidIsbn13() {
        String validIsbn13 = "9780140449112"; // The Odyssey ISBN-13 - valid checksum
        assertDoesNotThrow(() -> book.setIsbn(validIsbn13));
        assertEquals(validIsbn13, book.getIsbn());
    }

    @Test
    void testSetIsbnValidIsbn13WithHyphens() {
        String validIsbn13 = "978-0-140-44911-2"; // The Odyssey with hyphens
        assertDoesNotThrow(() -> book.setIsbn(validIsbn13));
        assertEquals(validIsbn13, book.getIsbn());
    }

    @Test
    void testSetIsbnNull() {
        assertDoesNotThrow(() -> book.setIsbn(null));
        assertNull(book.getIsbn());
    }

    // ISBN setter tests - Invalid cases
    @Test
    void testSetIsbnBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setIsbn("   ")
        );
        assertEquals("ISBN cannot be blank", exception.getMessage());
    }

    @Test
    void testSetIsbnEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setIsbn("")
        );
        assertEquals("ISBN cannot be blank", exception.getMessage());
    }

    @Test
    void testSetIsbnInvalidLength() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setIsbn("12345")
        );
        assertEquals("ISBN must be a valid ISBN-10 or ISBN-13 format", exception.getMessage());
    }

    @Test
    void testSetIsbnInvalidCharacters() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setIsbn("012345678A")
        );
        assertEquals("ISBN must be a valid ISBN-10 or ISBN-13 format", exception.getMessage());
    }

    @Test
    void testSetIsbnInvalidChecksum10() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setIsbn("0140449115") // Wrong checksum (should be 6)
        );
        assertEquals("ISBN must be a valid ISBN-10 or ISBN-13 format", exception.getMessage());
    }

    @Test
    void testSetIsbnInvalidChecksum13() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setIsbn("9780140449113") // Wrong checksum (should be 2)
        );
        assertEquals("ISBN must be a valid ISBN-10 or ISBN-13 format", exception.getMessage());
    }

    @Test
    void testSetIsbnTooLong() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setIsbn("97801234567890") // Too long
        );
        assertEquals("ISBN must be a valid ISBN-10 or ISBN-13 format", exception.getMessage());
    }

    @Test
    void testSetIsbnInvalidIsbn13Format() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> book.setIsbn("978012345678X") // X not allowed in ISBN-13
        );
        assertEquals("ISBN must be a valid ISBN-10 or ISBN-13 format", exception.getMessage());
    }

    // Test toString method
    @Test
    void testToString() {
        book.setPublisher("Test Publisher");
        book.setPublicationYear(2023);
        book.setCity("New York");
        book.setEdition("1st Edition");
        book.setIsbn("0140449116"); // Valid ISBN-10

        String result = book.toString();
        assertNotNull(result);
        assertTrue(result.contains("Book{"));
        assertTrue(result.contains("publisher='Test Publisher'"));
        assertTrue(result.contains("publicationYear=2023"));
        assertTrue(result.contains("city='New York'"));
        assertTrue(result.contains("edition='1st Edition'"));
        assertTrue(result.contains("isbn='0140449116'"));
    }

    // Integration test with all valid values
    @Test
    void testCompleteBookSetup() {
        Book completeBook = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        completeBook.setPublisher("Scribner");
        completeBook.setPublicationYear(1925);
        completeBook.setCity("New York");
        completeBook.setEdition("First Edition");
        completeBook.setIsbn("0140449116"); // Valid ISBN-10

        assertEquals("The Great Gatsby", completeBook.getTitle());
        assertEquals("F. Scott Fitzgerald", completeBook.getAuthor());
        assertEquals("Scribner", completeBook.getPublisher());
        assertEquals(Integer.valueOf(1925), completeBook.getPublicationYear());
        assertEquals("New York", completeBook.getCity());
        assertEquals("First Edition", completeBook.getEdition());
        assertEquals("0140449116", completeBook.getIsbn());
    }

    // Test with real ISBN examples
    @Test
    void testRealIsbnExamples() {
        // Real ISBN-10 examples with correct checksums
        assertDoesNotThrow(() -> book.setIsbn("0140449116")); // The Odyssey
        assertDoesNotThrow(() -> book.setIsbn("0679732241")); // Beloved

        // Real ISBN-13 examples with correct checksums
        assertDoesNotThrow(() -> book.setIsbn("9780140449112")); // The Odyssey
        assertDoesNotThrow(() -> book.setIsbn("9780679732242")); // Beloved
    }
}
