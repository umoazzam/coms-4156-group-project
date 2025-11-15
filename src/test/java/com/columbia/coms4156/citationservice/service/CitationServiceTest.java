package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.Citation;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.repository.ArticleRepository;
import com.columbia.coms4156.citationservice.repository.BookRepository;
import com.columbia.coms4156.citationservice.repository.CitationRepository;
import com.columbia.coms4156.citationservice.repository.SubmissionRepository;
import com.columbia.coms4156.citationservice.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CitationServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private VideoRepository videoRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private CitationRepository citationRepository;
    @Mock
    private SubmissionRepository submissionRepository;
    @Mock
    private GoogleBooksService googleBooksService;

    @InjectMocks
    private CitationService citationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFormatAuthorNameSingleAuthor() {
        String author = "John Doe";
        String formattedName = citationService.formatAuthorName(author);
        assertEquals("Doe, John", formattedName);
    }

    @Test
    void testFormatAuthorNameMultipleAuthors() {
        String authors = "John Doe, Jane Smith";
        String formattedNames = citationService.formatAuthorName(authors);
        assertEquals("Doe, John and Smith, Jane", formattedNames);
    }

    @Test
    void testFormatAPAAuthorNameSingleAuthor() {
        String author = "John Doe";
        String formattedName = citationService.formatAPAAuthorName(author);
        assertEquals("Doe, J.", formattedName);
    }

    @Test
    void testFormatAPAAuthorNameTwoAuthors() {
        String authors = "John Doe, Jane Smith";
        String formattedNames = citationService.formatAPAAuthorName(authors);
        assertEquals("Doe, J. & Smith, J.", formattedNames);
    }

    @Test
    void testFormatAPAAuthorNameMultipleAuthors() {
        String authors = "John Doe, Jane Smith, Peter Jones";
        String formattedNames = citationService.formatAPAAuthorName(authors);
        assertEquals("Doe, J., Smith, J. & Jones, P.", formattedNames);
    }

    @Test
    void testGenerateMLACitationForBook() {
        Book book = new Book("The Book Title", "John Doe, Jane Smith");
        book.setPublisher("The Publisher");
        book.setPublicationYear(2023);
        String citation = citationService.generateMLACitation(book);
        assertEquals("Doe, John and Smith, Jane. _The Book Title_. The Publisher, 2023.", citation);
    }

    @Test
    void testGenerateCitationByStyleForBook() {
        Book book = new Book("The Book Title", "John Doe, Jane Smith");
        book.setPublisher("The Publisher");
        book.setPublicationYear(2023);
        String citation = citationService.generateCitationByStyle(book, "APA");
        assertEquals("Doe, J. & Smith, J. (2023). The Book Title. The Publisher.", citation);
    }

    @Test
    void testGenerateAPACitationForBook() {
        Book book = new Book("The Book Title", "John Doe, Jane Smith");
        book.setPublisher("The Publisher");
        book.setPublicationYear(2023);
        String citation = citationService.generateAPACitation(book);
        assertEquals("Doe, J. & Smith, J. (2023). The Book Title. The Publisher.", citation);
    }

    @Test
    void testGenerateMLACitationForVideo() {
        Video video = new Video("The Video Title", "John Doe");
        video.setPlatform("YouTube");
        video.setReleaseYear(2023);
        String citation = citationService.generateMLACitation(video);
        assertEquals("Doe, John. _The Video Title_. YouTube, 2023.", citation);
    }

    @Test
    void testGenerateMLACitationForArticle() {
        Article article = new Article("The Article Title", "John Doe");
        article.setJournal("The Journal");
        article.setVolume("1");
        article.setIssue("2");
        article.setPublicationYear(2023);
        String citation = citationService.generateMLACitation(article);
        assertEquals("Doe, John. \"The Article Title.\" The Journal, vol. 1, no. 2, 2023.", citation);
    }

    @Test
    void testGenerateAPACitationForVideo() {
        Video video = new Video("The Video Title", "John Doe");
        video.setPlatform("YouTube");
        video.setReleaseYear(2023);
        String citation = citationService.generateAPACitation(video);
        assertEquals("Doe, J. (2023). The Video Title [Video]. YouTube.", citation);
    }

    @Test
    void testGenerateAPACitationForArticle() {
        Article article = new Article("The Article Title", "John Doe");
        article.setJournal("The Journal");
        article.setVolume("1");
        article.setIssue("2");
        article.setPublicationYear(2023);
        String citation = citationService.generateAPACitation(article);
        assertEquals("Doe, J. (2023). The Article Title. The Journal, 1(2).", citation);
    }

    @Test
    void testGenerateChicagoCitationForBook() {
        Book book = new Book("The Book Title", "John Doe, Jane Smith");
        book.setCity("New York");
        book.setPublisher("The Publisher");
        book.setPublicationYear(2023);
        String citation = citationService.generateChicagoCitation(book);
        assertEquals("Doe, John and Smith, Jane. \"The Book Title.\" New York: The Publisher, 2023.", citation);
    }

    @Test
    void testGenerateChicagoCitationForVideo() {
        Video video = new Video("The Video Title", "John Doe");
        video.setPlatform("YouTube");
        video.setReleaseYear(2023);
        String citation = citationService.generateChicagoCitation(video);
        assertEquals("Doe, John. \"The Video Title.\" YouTube, 2023.", citation);
    }

    @Test
    void testGenerateCitationForSourceWithBackfillNoApiResult() {
        Long sourceId = 2L;
        Long bookId = 101L;
        String isbn = "9780140449112";
        String style = "MLA";

        Book storedBook = new Book("Original Title", "Original Author");
        storedBook.setId(bookId);
        storedBook.setIsbn(isbn);
        storedBook.setPublisher("Original Publisher");
        storedBook.setPublicationYear(2000);

        Citation citation = new Citation();
        citation.setId(sourceId);
        citation.setMediaId(bookId);
        citation.setMediaType("book");

        when(citationRepository.findById(sourceId)).thenReturn(Optional.of(citation));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(storedBook));
        when(googleBooksService.fetchBookDataByIsbn(isbn)).thenReturn(Mono.empty());

        String expectedCitation = "Author, Original. _Original Title_. Original Publisher, 2000.";
        assertEquals(expectedCitation, citationService.generateCitationForSource(sourceId, style, true).getCitationString());
    }

    @Test
    void testGenerateCitationForSourceWithBackfillNoIsbn() {
        Long sourceId = 3L;
        Long bookId = 102L;
        String style = "MLA";

        Book storedBook = new Book("Original Title", "Original Author");
        storedBook.setId(bookId);
        storedBook.setPublisher("Original Publisher");
        storedBook.setPublicationYear(2000);
        // No ISBN set

        Citation citation = new Citation();
        citation.setId(sourceId);
        citation.setMediaId(bookId);
        citation.setMediaType("book");

        when(citationRepository.findById(sourceId)).thenReturn(Optional.of(citation));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(storedBook));
        // googleBooksService.fetchBookDataByIsbn should not be called

        String expectedCitation = "Author, Original. _Original Title_. Original Publisher, 2000.";
        assertEquals(expectedCitation, citationService.generateCitationForSource(sourceId, style, true).getCitationString());
    }

    @Test
    void testGenerateCitationForSourceNoBackfill() {
        Long sourceId = 4L;
        Long bookId = 103L;
        String style = "MLA";

        Book storedBook = new Book("Original Title", "Original Author");
        storedBook.setId(bookId);
        storedBook.setIsbn("978-0-306-40615-7"); // Has ISBN but backfill is false
        storedBook.setPublisher("Original Publisher");
        storedBook.setPublicationYear(2000);

        Citation citation = new Citation();
        citation.setId(sourceId);
        citation.setMediaId(bookId);
        citation.setMediaType("book");

        when(citationRepository.findById(sourceId)).thenReturn(Optional.of(citation));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(storedBook));
        // googleBooksService.fetchBookDataByIsbn should not be called

        String expectedCitation = "Author, Original. _Original Title_. Original Publisher, 2000.";
        assertEquals(expectedCitation, citationService.generateCitationForSource(sourceId, style, false).getCitationString());
    }

}
