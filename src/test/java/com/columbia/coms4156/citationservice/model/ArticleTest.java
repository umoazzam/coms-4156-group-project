package com.columbia.coms4156.citationservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Article class.
 * Tests all setter methods and their parameter validation.
 */
class ArticleTest {

    private Article article;

    @BeforeEach
    void setUp() {
        article = new Article();
    }

    @Test
    void testDefaultConstructor() {
        Article newArticle = new Article();
        assertNotNull(newArticle);
        assertNull(newArticle.getJournal());
        assertNull(newArticle.getVolume());
        assertNull(newArticle.getIssue());
        assertNull(newArticle.getPages());
        assertNull(newArticle.getDoi());
        assertNull(newArticle.getUrl());
        assertNull(newArticle.getPublicationYear());
    }

    @Test
    void testParameterizedConstructor() {
        String title = "Test Article";
        String author = "Test Author";
        Article newArticle = new Article(title, author);

        assertNotNull(newArticle);
        assertEquals(title, newArticle.getTitle());
        assertEquals(author, newArticle.getAuthor());
    }

    // Journal setter tests
    @Test
    void testSetJournalValid() {
        String validJournal = "Nature";
        article.setJournal(validJournal);
        assertEquals(validJournal, article.getJournal());
    }

    @Test
    void testSetJournalNull() {
        assertDoesNotThrow(() -> article.setJournal(null));
        assertNull(article.getJournal());
    }

    @Test
    void testSetJournalBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setJournal("   ")
        );
        assertEquals("Journal name cannot be blank", exception.getMessage());
    }

    @Test
    void testSetJournalEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setJournal("")
        );
        assertEquals("Journal name cannot be blank", exception.getMessage());
    }

    // Volume setter tests
    @Test
    void testSetVolumeValid() {
        String validVolume = "42";
        article.setVolume(validVolume);
        assertEquals(validVolume, article.getVolume());
    }

    @Test
    void testSetVolumeNull() {
        assertDoesNotThrow(() -> article.setVolume(null));
        assertNull(article.getVolume());
    }

    @Test
    void testSetVolumeBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setVolume("   ")
        );
        assertEquals("Volume number cannot be blank", exception.getMessage());
    }

    @Test
    void testSetVolumeEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setVolume("")
        );
        assertEquals("Volume number cannot be blank", exception.getMessage());
    }

    // Issue setter tests
    @Test
    void testSetIssueValid() {
        String validIssue = "3";
        article.setIssue(validIssue);
        assertEquals(validIssue, article.getIssue());
    }

    @Test
    void testSetIssueNull() {
        assertDoesNotThrow(() -> article.setIssue(null));
        assertNull(article.getIssue());
    }

    @Test
    void testSetIssueBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setIssue("   ")
        );
        assertEquals("Issue number cannot be blank", exception.getMessage());
    }

    @Test
    void testSetIssueEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setIssue("")
        );
        assertEquals("Issue number cannot be blank", exception.getMessage());
    }

    // Pages setter tests
    @Test
    void testSetPagesValid() {
        String validPages = "123-145";
        article.setPages(validPages);
        assertEquals(validPages, article.getPages());
    }

    @Test
    void testSetPagesNull() {
        assertDoesNotThrow(() -> article.setPages(null));
        assertNull(article.getPages());
    }

    @Test
    void testSetPagesBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setPages("   ")
        );
        assertEquals("Page numbers cannot be blank", exception.getMessage());
    }

    @Test
    void testSetPagesEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setPages("")
        );
        assertEquals("Page numbers cannot be blank", exception.getMessage());
    }

    // DOI setter tests
    @Test
    void testSetDoiValid() {
        String validDoi = "10.1038/nature12373";
        article.setDoi(validDoi);
        assertEquals(validDoi, article.getDoi());
    }

    @Test
    void testSetDoiNull() {
        assertDoesNotThrow(() -> article.setDoi(null));
        assertNull(article.getDoi());
    }

    @Test
    void testSetDoiBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setDoi("   ")
        );
        assertEquals("DOI cannot be blank", exception.getMessage());
    }

    @Test
    void testSetDoiEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setDoi("")
        );
        assertEquals("DOI cannot be blank", exception.getMessage());
    }

    @Test
    void testSetDoiInvalidFormat() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setDoi("invalid-doi")
        );
        assertEquals("DOI must follow standard format (e.g., 10.1000/123456)", exception.getMessage());
    }

    @Test
    void testSetDoiInvalidPrefix() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setDoi("11.1234/test")
        );
        assertEquals("DOI must follow standard format (e.g., 10.1000/123456)", exception.getMessage());
    }

    @Test
    void testSetDoiMissingSlash() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setDoi("10.1234test")
        );
        assertEquals("DOI must follow standard format (e.g., 10.1000/123456)", exception.getMessage());
    }

    @Test
    void testSetDoiShortRegistrantCode() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setDoi("10.123/test")
        );
        assertEquals("DOI must follow standard format (e.g., 10.1000/123456)", exception.getMessage());
    }

    @Test
    void testSetDoiValidFormats() {
        // Test various valid DOI formats
        assertDoesNotThrow(() -> article.setDoi("10.1000/123456"));
        assertEquals("10.1000/123456", article.getDoi());

        assertDoesNotThrow(() -> article.setDoi("10.1038/nature12373"));
        assertEquals("10.1038/nature12373", article.getDoi());

        assertDoesNotThrow(() -> article.setDoi("10.1126/science.aac4716"));
        assertEquals("10.1126/science.aac4716", article.getDoi());
    }

    // URL setter tests
    @Test
    void testSetUrlValid() {
        String validUrl = "https://www.nature.com/articles/nature12373";
        article.setUrl(validUrl);
        assertEquals(validUrl, article.getUrl());
    }

    @Test
    void testSetUrlNull() {
        assertDoesNotThrow(() -> article.setUrl(null));
        assertNull(article.getUrl());
    }

    @Test
    void testSetUrlBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setUrl("   ")
        );
        assertEquals("URL cannot be blank", exception.getMessage());
    }

    @Test
    void testSetUrlEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setUrl("")
        );
        assertEquals("URL cannot be blank", exception.getMessage());
    }

    @Test
    void testSetUrlInvalidFormat() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setUrl("not-a-url")
        );
        assertEquals("URL must be a valid HTTP or HTTPS URL", exception.getMessage());
    }

    @Test
    void testSetUrlMissingProtocol() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setUrl("www.example.com")
        );
        assertEquals("URL must be a valid HTTP or HTTPS URL", exception.getMessage());
    }

    @Test
    void testSetUrlInvalidProtocol() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setUrl("ftp://example.com")
        );
        assertEquals("URL must be a valid HTTP or HTTPS URL", exception.getMessage());
    }

    @Test
    void testSetUrlValidHttp() {
        String validHttpUrl = "http://www.example.com";
        assertDoesNotThrow(() -> article.setUrl(validHttpUrl));
        assertEquals(validHttpUrl, article.getUrl());
    }

    @Test
    void testSetUrlValidHttps() {
        String validHttpsUrl = "https://www.example.com";
        assertDoesNotThrow(() -> article.setUrl(validHttpsUrl));
        assertEquals(validHttpsUrl, article.getUrl());
    }

    @Test
    void testSetUrlValidComplexUrl() {
        String complexUrl = "https://www.nature.com/articles/nature12373?tab=references&ref=pdf";
        assertDoesNotThrow(() -> article.setUrl(complexUrl));
        assertEquals(complexUrl, article.getUrl());
    }

    // Publication year setter tests
    @Test
    void testSetPublicationYearValid() {
        Integer validYear = 2023;
        article.setPublicationYear(validYear);
        assertEquals(validYear, article.getPublicationYear());
    }

    @Test
    void testSetPublicationYearMinimumValid() {
        Integer minimumYear = 1000;
        assertDoesNotThrow(() -> article.setPublicationYear(minimumYear));
        assertEquals(minimumYear, article.getPublicationYear());
    }

    @Test
    void testSetPublicationYearCurrentYear() {
        Integer currentYear = java.time.Year.now().getValue();
        assertDoesNotThrow(() -> article.setPublicationYear(currentYear));
        assertEquals(currentYear, article.getPublicationYear());
    }

    @Test
    void testSetPublicationYearFutureValid() {
        Integer futureYear = java.time.Year.now().getValue() + 5;
        assertDoesNotThrow(() -> article.setPublicationYear(futureYear));
        assertEquals(futureYear, article.getPublicationYear());
    }

    @Test
    void testSetPublicationYearNull() {
        assertDoesNotThrow(() -> article.setPublicationYear(null));
        assertNull(article.getPublicationYear());
    }

    @Test
    void testSetPublicationYearTooOld() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setPublicationYear(999)
        );
        assertTrue(exception.getMessage().contains("Publication year must be between 1000 and"));
    }

    @Test
    void testSetPublicationYearTooFuture() {
        Integer tooFutureYear = java.time.Year.now().getValue() + 15;
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setPublicationYear(tooFutureYear)
        );
        assertTrue(exception.getMessage().contains("Publication year must be between 1000 and"));
    }

    @Test
    void testSetPublicationYearNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setPublicationYear(-1)
        );
        assertTrue(exception.getMessage().contains("Publication year must be between 1000 and"));
    }

    @Test
    void testSetPublicationYearZero() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> article.setPublicationYear(0)
        );
        assertTrue(exception.getMessage().contains("Publication year must be between 1000 and"));
    }

    // Test toString method
    @Test
    void testToString() {
        article.setJournal("Nature");
        article.setVolume("42");
        article.setIssue("3");
        article.setPages("123-145");
        article.setDoi("10.1038/nature12373");
        article.setUrl("https://www.nature.com/articles/nature12373");
        article.setPublicationYear(2023);

        String result = article.toString();
        assertNotNull(result);
        assertTrue(result.contains("Article{"));
        assertTrue(result.contains("journal='Nature'"));
        assertTrue(result.contains("volume='42'"));
        assertTrue(result.contains("issue='3'"));
        assertTrue(result.contains("pages='123-145'"));
        assertTrue(result.contains("doi='10.1038/nature12373'"));
        assertTrue(result.contains("url='https://www.nature.com/articles/nature12373'"));
        assertTrue(result.contains("publicationYear=2023"));
    }

    // Integration test with all valid values
    @Test
    void testCompleteArticleSetup() {
        Article completeArticle = new Article("Test Title", "Test Author");
        completeArticle.setJournal("Science");
        completeArticle.setVolume("350");
        completeArticle.setIssue("6262");
        completeArticle.setPages("800-805");
        completeArticle.setDoi("10.1126/science.aac4716");
        completeArticle.setUrl("https://science.sciencemag.org/content/350/6262/800");
        completeArticle.setPublicationYear(2015);

        assertEquals("Test Title", completeArticle.getTitle());
        assertEquals("Test Author", completeArticle.getAuthor());
        assertEquals("Science", completeArticle.getJournal());
        assertEquals("350", completeArticle.getVolume());
        assertEquals("6262", completeArticle.getIssue());
        assertEquals("800-805", completeArticle.getPages());
        assertEquals("10.1126/science.aac4716", completeArticle.getDoi());
        assertEquals("https://science.sciencemag.org/content/350/6262/800", completeArticle.getUrl());
        assertEquals(Integer.valueOf(2015), completeArticle.getPublicationYear());
    }
}
