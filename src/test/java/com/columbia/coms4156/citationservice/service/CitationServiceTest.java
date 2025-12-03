package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.Citation;
import com.columbia.coms4156.citationservice.model.GroupCitationResponse;
import com.columbia.coms4156.citationservice.model.Submission;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Test
    void testFormatAuthorNameSingleName() {
        String author = "Madonna";
        String formattedName = citationService.formatAuthorName(author);
        assertEquals("Madonna", formattedName);
    }

    @Test
    void testFormatAPAAuthorNameSingleName() {
        String author = "Madonna";
        String formattedName = citationService.formatAPAAuthorName(author);
        assertEquals("Madonna", formattedName);
    }

    @Test
    void testGenerateMLACitationForBookWithoutPublisher() {
        Book book = new Book("The Book Title", "John Doe");
        book.setPublicationYear(2023);
        String citation = citationService.generateMLACitation(book);
        assertEquals("Doe, John. _The Book Title_. 2023.", citation);
    }

    @Test
    void testGenerateMLACitationForBookWithoutYear() {
        Book book = new Book("The Book Title", "John Doe");
        book.setPublisher("The Publisher");
        String citation = citationService.generateMLACitation(book);
        assertEquals("Doe, John. _The Book Title_. The Publisher.", citation);
    }


    @Test
    void testGenerateMLACitationForVideoWithoutPlatform() {
        Video video = new Video("The Video Title", "John Doe");
        video.setReleaseYear(2023);
        String citation = citationService.generateMLACitation(video);
        assertEquals("Doe, John. _The Video Title_. 2023.", citation);
    }

    @Test
    void testGenerateMLACitationForVideoWithoutYear() {
        Video video = new Video("The Video Title", "John Doe");
        video.setPlatform("YouTube");
        String citation = citationService.generateMLACitation(video);
        assertEquals("Doe, John. _The Video Title_. YouTube.", citation);
    }

    @Test
    void testGenerateMLACitationForArticleWithoutJournal() {
        Article article = new Article("The Article Title", "John Doe");
        article.setPublicationYear(2023);
        String citation = citationService.generateMLACitation(article);
        assertEquals("Doe, John. \"The Article Title.\"", citation);
    }

    @Test
    void testGenerateMLACitationForArticleWithoutVolume() {
        Article article = new Article("The Article Title", "John Doe");
        article.setJournal("The Journal");
        article.setIssue("2");
        article.setPublicationYear(2023);
        String citation = citationService.generateMLACitation(article);
        assertEquals("Doe, John. \"The Article Title.\" The Journal, no. 2, 2023.", citation);
    }

    @Test
    void testGenerateAPACitationForBookWithCity() {
        Book book = new Book("The Book Title", "John Doe");
        book.setPublisher("The Publisher");
        book.setCity("New York");
        book.setPublicationYear(2023);
        String citation = citationService.generateAPACitation(book);
        assertEquals("Doe, J. (2023). The Book Title. The Publisher, New York.", citation);
    }

    @Test
    void testGenerateAPACitationForBookWithoutPublisher() {
        Book book = new Book("The Book Title", "John Doe");
        book.setPublicationYear(2023);
        String citation = citationService.generateAPACitation(book);
        assertEquals("Doe, J. (2023). The Book Title.", citation);
    }

    @Test
    void testGenerateAPACitationForArticleWithoutIssue() {
        Article article = new Article("The Article Title", "John Doe");
        article.setJournal("The Journal");
        article.setVolume("1");
        article.setPublicationYear(2023);
        String citation = citationService.generateAPACitation(article);
        assertEquals("Doe, J. (2023). The Article Title. The Journal, 1.", citation);
    }

    @Test
    void testGenerateChicagoCitationForBookWithoutCity() {
        Book book = new Book("The Book Title", "John Doe");
        book.setPublisher("The Publisher");
        book.setPublicationYear(2023);
        String citation = citationService.generateChicagoCitation(book);
        assertEquals("Doe, John. \"The Book Title.\" 2023.", citation);
    }

    @Test
    void testGenerateChicagoCitationForArticleWithoutVolume() {
        Article article = new Article("The Article Title", "John Doe");
        article.setJournal("The Journal");
        article.setIssue("2");
        article.setPublicationYear(2023);
        String citation = citationService.generateChicagoCitation(article);
        assertEquals("Doe, John. \"The Article Title.\" The Journal, no. 2 (2023).", citation);
    }

    @Test
    void testGenerateCitationByStyleForVideoAPA() {
        Video video = new Video("The Video Title", "John Doe");
        video.setPlatform("YouTube");
        video.setReleaseYear(2023);
        String citation = citationService.generateCitationByStyle(video, "APA");
        assertEquals("Doe, J. (2023). The Video Title [Video]. YouTube.", citation);
    }

    @Test
    void testGenerateCitationByStyleForVideoChicago() {
        Video video = new Video("The Video Title", "John Doe");
        video.setPlatform("YouTube");
        video.setReleaseYear(2023);
        String citation = citationService.generateCitationByStyle(video, "CHICAGO");
        assertEquals("Doe, John. \"The Video Title.\" YouTube, 2023.", citation);
    }

    @Test
    void testGenerateCitationByStyleForArticleAPA() {
        Article article = new Article("The Article Title", "John Doe");
        article.setJournal("The Journal");
        article.setVolume("1");
        article.setIssue("2");
        article.setPublicationYear(2023);
        String citation = citationService.generateCitationByStyle(article, "APA");
        assertEquals("Doe, J. (2023). The Article Title. The Journal, 1(2).", citation);
    }

    @Test
    void testGenerateCitationByStyleForArticleChicago() {
        Article article = new Article("The Article Title", "John Doe");
        article.setJournal("The Journal");
        article.setVolume("1");
        article.setIssue("2");
        article.setPublicationYear(2023);
        String citation = citationService.generateCitationByStyle(article, "CHICAGO");
        assertEquals("Doe, John. \"The Article Title.\" The Journal 1, no. 2 (2023).", citation);
    }

    @Test
    void testGenerateCitationByStyleForBookChicago() {
        Book book = new Book("The Book Title", "John Doe");
        book.setCity("New York");
        book.setPublisher("The Publisher");
        book.setPublicationYear(2023);
        String citation = citationService.generateCitationByStyle(book, "CHICAGO");
        assertEquals("Doe, John. \"The Book Title.\" New York: The Publisher, 2023.", citation);
    }

    @Test
    void testGenerateAPACitationForArticleWithoutVolumeAndIssue() {
        Article article = new Article("The Article Title", "John Doe");
        article.setJournal("The Journal");
        article.setPublicationYear(2023);
        String citation = citationService.generateAPACitation(article);
        assertEquals("Doe, J. (2023). The Article Title. The Journal.", citation);
    }

    @Test
    void testGenerateChicagoCitationForArticleWithoutIssue() {
        Article article = new Article("The Article Title", "John Doe");
        article.setJournal("The Journal");
        article.setVolume("1");
        article.setPublicationYear(2023);
        String citation = citationService.generateChicagoCitation(article);
        assertEquals("Doe, John. \"The Article Title.\" The Journal 1 (2023).", citation);
    }


    @Test
    void testGenerateCitationsForGroup() {
        Long submissionId = 1L;
        String style = "MLA";

        Submission submission = new Submission();
        submission.setId(submissionId);

        Citation citation1 = new Citation();
        citation1.setId(1L);
        citation1.setMediaId(101L);
        citation1.setMediaType("book");

        Citation citation2 = new Citation();
        citation2.setId(2L);
        citation2.setMediaId(201L);
        citation2.setMediaType("video");

        List<Citation> citations = new ArrayList<>();
        citations.add(citation1);
        citations.add(citation2);
        submission.setCitations(citations);

        Book book = new Book("Test Book", "Test Author");
        book.setId(101L);
        book.setPublisher("Test Publisher");
        book.setPublicationYear(2020);

        Video video = new Video("Test Video", "Test Author");
        video.setId(201L);
        video.setPlatform("YouTube");
        video.setReleaseYear(2021);

        when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));
        when(bookRepository.findById(101L)).thenReturn(Optional.of(book));
        when(videoRepository.findById(201L)).thenReturn(Optional.of(video));

        GroupCitationResponse response = citationService.generateCitationsForGroup(submissionId, style, false);
        assertNotNull(response);
        assertEquals(submissionId, response.getSubmissionId());
        assertEquals(2, response.getCitations().size());
    }

    @Test
    void testGenerateCitationsForGroupWithAPA() {
        Long submissionId = 2L;
        String style = "APA";

        Submission submission = new Submission();
        submission.setId(submissionId);

        Citation citation = new Citation();
        citation.setId(3L);
        citation.setMediaId(102L);
        citation.setMediaType("book");

        List<Citation> citations = new ArrayList<>();
        citations.add(citation);
        submission.setCitations(citations);

        Book book = new Book("Test Book", "Test Author");
        book.setId(102L);
        book.setPublisher("Test Publisher");
        book.setPublicationYear(2020);

        when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));
        when(bookRepository.findById(102L)).thenReturn(Optional.of(book));

        GroupCitationResponse response = citationService.generateCitationsForGroup(submissionId, style, false);
        assertNotNull(response);
        assertEquals(submissionId, response.getSubmissionId());
        assertEquals(1, response.getCitations().size());
    }

    @Test
    void testGenerateCitationsForGroupWithArticle() {
        Long submissionId = 3L;
        String style = "CHICAGO";

        Submission submission = new Submission();
        submission.setId(submissionId);

        Citation citation = new Citation();
        citation.setId(4L);
        citation.setMediaId(301L);
        citation.setMediaType("article");

        List<Citation> citations = new ArrayList<>();
        citations.add(citation);
        submission.setCitations(citations);

        Article article = new Article("Test Article", "Test Author");
        article.setId(301L);
        article.setJournal("Test Journal");
        article.setPublicationYear(2022);

        when(submissionRepository.findById(submissionId)).thenReturn(Optional.of(submission));
        when(articleRepository.findById(301L)).thenReturn(Optional.of(article));

        GroupCitationResponse response = citationService.generateCitationsForGroup(submissionId, style, false);
        assertNotNull(response);
        assertEquals(submissionId, response.getSubmissionId());
        assertEquals(1, response.getCitations().size());
    }


    @Test
    void testGenerateAPACitationForBookWithoutYear() {
        Book book = new Book("The Book Title", "John Doe");
        book.setPublisher("The Publisher");
        String citation = citationService.generateAPACitation(book);
        assertEquals("Doe, J. The Book Title. The Publisher.", citation);
    }

    @Test
    void testGenerateAPACitationForVideoWithoutYear() {
        Video video = new Video("The Video Title", "John Doe");
        video.setPlatform("YouTube");
        String citation = citationService.generateAPACitation(video);
        assertEquals("Doe, J. The Video Title [Video]. YouTube.", citation);
    }

    @Test
    void testGenerateAPACitationForArticleWithoutYear() {
        Article article = new Article("The Article Title", "John Doe");
        article.setJournal("The Journal");
        String citation = citationService.generateAPACitation(article);
        assertEquals("Doe, J. The Article Title. The Journal.", citation);
    }

    @Test
    void testGenerateChicagoCitationForBookWithoutYear() {
        Book book = new Book("The Book Title", "John Doe");
        book.setCity("New York");
        book.setPublisher("The Publisher");
        String citation = citationService.generateChicagoCitation(book);
        assertEquals("Doe, John. \"The Book Title.\" New York: The Publisher,", citation);
    }

    @Test
    void testGenerateChicagoCitationForVideoWithoutYear() {
        Video video = new Video("The Video Title", "John Doe");
        video.setPlatform("YouTube");
        String citation = citationService.generateChicagoCitation(video);
        assertEquals("Doe, John. \"The Video Title.\" YouTube,", citation);
    }

    @Test
    void testGenerateChicagoCitationForArticleWithoutYear() {
        Article article = new Article("The Article Title", "John Doe");
        article.setJournal("The Journal");
        article.setVolume("1");
        String citation = citationService.generateChicagoCitation(article);
        assertEquals("Doe, John. \"The Article Title.\" The Journal 1 (", citation);
    }

}
