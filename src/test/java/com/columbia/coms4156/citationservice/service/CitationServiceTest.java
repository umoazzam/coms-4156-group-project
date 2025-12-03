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
    @Mock
    private CrossRefDoiService crossRefDoiService;

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
    void testGenerateCitationForBookWithBackfillNoApiResult() {
        Long citationId = 2L;
        Long bookId = 101L;
        String isbn = "9780140449112";
        String style = "MLA";

        Book storedBook = new Book("Original Title", "Original Author");
        storedBook.setId(bookId);
        storedBook.setIsbn(isbn);
        storedBook.setPublisher("Original Publisher");
        storedBook.setPublicationYear(2000);

        Citation citation = new Citation();
        citation.setId(citationId);
        citation.setMediaId(bookId);
        citation.setMediaType("book");

        when(citationRepository.findById(citationId)).thenReturn(Optional.of(citation));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(storedBook));
        when(googleBooksService.fetchBookDataByIsbn(isbn)).thenReturn(Mono.empty());

        String expectedCitation = "Author, Original. _Original Title_. Original Publisher, 2000.";
        assertEquals(expectedCitation, citationService.generateCitationForSource(citationId, style, true).getCitationString());
    }

    @Test
    void testGenerateCitationForBookWithBackfillNoIsbn() {
        Long citationId = 3L;
        Long bookId = 102L;
        String style = "MLA";

        Book storedBook = new Book("Original Title", "Original Author");
        storedBook.setId(bookId);
        storedBook.setPublisher("Original Publisher");
        storedBook.setPublicationYear(2000);
        // No ISBN set

        Citation citation = new Citation();
        citation.setId(citationId);
        citation.setMediaId(bookId);
        citation.setMediaType("book");

        when(citationRepository.findById(citationId)).thenReturn(Optional.of(citation));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(storedBook));
        // googleBooksService.fetchBookDataByIsbn should not be called

        String expectedCitation = "Author, Original. _Original Title_. Original Publisher, 2000.";
        assertEquals(expectedCitation, citationService.generateCitationForSource(citationId, style, true).getCitationString());
    }

    @Test
    void testGenerateCitationForBookNoBackfill() {
        Long citationId = 4L;
        Long bookId = 103L;
        String style = "MLA";

        Book storedBook = new Book("Original Title", "Original Author");
        storedBook.setId(bookId);
        storedBook.setIsbn("978-0-306-40615-7"); // Has ISBN but backfill is false
        storedBook.setPublisher("Original Publisher");
        storedBook.setPublicationYear(2000);

        Citation citation = new Citation();
        citation.setId(citationId);
        citation.setMediaId(bookId);
        citation.setMediaType("book");

        when(citationRepository.findById(citationId)).thenReturn(Optional.of(citation));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(storedBook));
        // googleBooksService.fetchBookDataByIsbn should not be called

        String expectedCitation = "Author, Original. _Original Title_. Original Publisher, 2000.";
        assertEquals(expectedCitation, citationService.generateCitationForSource(citationId, style, false).getCitationString());
    }

    // Article backfill tests
    @Test
    void testGenerateCitationForArticleWithBackfillNoApiResult() {
        Long citationId = 5L;
        Long articleId = 201L;
        String doi = "10.1234/test.5678";
        String style = "MLA";

        Article storedArticle = new Article("Original Article Title", "Original Author");
        storedArticle.setId(articleId);
        storedArticle.setDoi(doi);
        storedArticle.setJournal("Original Journal");
        storedArticle.setVolume("10");
        storedArticle.setIssue("3");
        storedArticle.setPublicationYear(2020);

        Citation citation = new Citation();
        citation.setId(citationId);
        citation.setMediaId(articleId);
        citation.setMediaType("article");

        when(citationRepository.findById(citationId)).thenReturn(Optional.of(citation));
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(storedArticle));
        when(crossRefDoiService.fetchArticleDataByDoi(doi)).thenReturn(Mono.empty());

        String expectedCitation = "Author, Original. \"Original Article Title.\" Original Journal, vol. 10, no. 3, 2020.";
        assertEquals(expectedCitation, citationService.generateCitationForSource(citationId, style, true).getCitationString());
    }

    @Test
    void testGenerateCitationForArticleWithBackfillNoDoi() {
        Long citationId = 6L;
        Long articleId = 202L;
        String style = "MLA";

        Article storedArticle = new Article("Original Article Title", "Original Author");
        storedArticle.setId(articleId);
        storedArticle.setJournal("Original Journal");
        storedArticle.setVolume("10");
        storedArticle.setIssue("3");
        storedArticle.setPublicationYear(2020);
        // No DOI set

        Citation citation = new Citation();
        citation.setId(citationId);
        citation.setMediaId(articleId);
        citation.setMediaType("article");

        when(citationRepository.findById(citationId)).thenReturn(Optional.of(citation));
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(storedArticle));
        // crossRefDoiService.fetchArticleDataByDoi should not be called

        String expectedCitation = "Author, Original. \"Original Article Title.\" Original Journal, vol. 10, no. 3, 2020.";
        assertEquals(expectedCitation, citationService.generateCitationForSource(citationId, style, true).getCitationString());
    }

    @Test
    void testGenerateCitationForArticleNoBackfill() {
        Long citationId = 7L;
        Long articleId = 203L;
        String style = "MLA";

        Article storedArticle = new Article("Original Article Title", "Original Author");
        storedArticle.setId(articleId);
        storedArticle.setDoi("10.1234/test.5678"); // Has DOI but backfill is false
        storedArticle.setJournal("Original Journal");
        storedArticle.setVolume("10");
        storedArticle.setIssue("3");
        storedArticle.setPublicationYear(2020);

        Citation citation = new Citation();
        citation.setId(citationId);
        citation.setMediaId(articleId);
        citation.setMediaType("article");

        when(citationRepository.findById(citationId)).thenReturn(Optional.of(citation));
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(storedArticle));
        // crossRefDoiService.fetchArticleDataByDoi should not be called

        String expectedCitation = "Author, Original. \"Original Article Title.\" Original Journal, vol. 10, no. 3, 2020.";
        assertEquals(expectedCitation, citationService.generateCitationForSource(citationId, style, false).getCitationString());
    }

}
