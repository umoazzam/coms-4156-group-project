package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.controller.dto.BulkSourceRequest;
import com.columbia.coms4156.citationservice.controller.dto.SourceDTO;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.Citation;
import com.columbia.coms4156.citationservice.model.Submission;
import com.columbia.coms4156.citationservice.repository.BookRepository;
import com.columbia.coms4156.citationservice.repository.CitationRepository;
import com.columbia.coms4156.citationservice.repository.SubmissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
class SourceServiceTest {

    @InjectMocks
    private SourceService sourceService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private GoogleBooksService googleBooksService;

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private CitationRepository citationRepository;


    @BeforeEach
    void setUp() {
    }

    @Test
    void saveBookWithBackfillFillsMissingData() {
        // Arrange
        Book bookToSave = new Book("Original Title", "Original Author");
        bookToSave.setIsbn("9780140449112");

        Book googleBook = new Book();
        googleBook.setTitle("Google Book Title");
        googleBook.setAuthor("Google Author");
        googleBook.setPublisher("Google Publisher");
        googleBook.setPublicationYear(2023);

        when(googleBooksService.fetchBookDataByIsbn("9780140449112")).thenReturn(Mono.just(googleBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Book savedBook = sourceService.saveBook(bookToSave, true);

        // Assert
        assertEquals("Original Title", savedBook.getTitle());
        assertEquals("Original Author", savedBook.getAuthor());
        assertEquals("Google Publisher", savedBook.getPublisher());
        assertEquals(2023, savedBook.getPublicationYear());
        assertEquals("9780140449112", savedBook.getIsbn());
    }

    @Test
    void saveBookWithBackfillDoesNotOverrideExistingData() {
        // Arrange
        Book bookToSave = new Book("Original Title", "Original Author");
        bookToSave.setIsbn("9780140449112");
        bookToSave.setPublisher("Original Publisher");

        Book googleBook = new Book();
        googleBook.setTitle("Google Book Title");
        googleBook.setAuthor("Google Author");
        googleBook.setPublisher("Google Publisher");
        googleBook.setPublicationYear(2023);

        when(googleBooksService.fetchBookDataByIsbn("9780140449112")).thenReturn(Mono.just(googleBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Book savedBook = sourceService.saveBook(bookToSave, true);

        // Assert
        assertEquals("Original Title", savedBook.getTitle());
        assertEquals("Original Author", savedBook.getAuthor());
        assertEquals("Original Publisher", savedBook.getPublisher());
        assertEquals(2023, savedBook.getPublicationYear());
    }

    @Test
    void saveBookWithoutBackfillDoesNotCallGoogleBooks() {
        // Arrange
        Book bookToSave = new Book("Original Title", "Original Author");
        bookToSave.setIsbn("9780140449112");

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Book savedBook = sourceService.saveBook(bookToSave, false);

        // Assert
        assertEquals("Original Title", savedBook.getTitle());
        verify(googleBooksService, never()).fetchBookDataByIsbn(anyString());
    }

    @Test
    void saveBookWithBackfillNoIsbnDoesNotCallGoogleBooks() {
        // Arrange
        Book bookToSave = new Book("Original Title", "Original Author");
        // No ISBN set, so backfill should not be called

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Book savedBook = sourceService.saveBook(bookToSave, true);

        // Assert
        assertEquals("Original Title", savedBook.getTitle());
        verify(googleBooksService, never()).fetchBookDataByIsbn(anyString());
    }

    @Test
    void addOrAppendSourcesWithBackfillFillsMissingData() {
        // Arrange
        SourceDTO sourceDTO = new SourceDTO();
        sourceDTO.setMediaType("book");
        sourceDTO.setTitle("Original Title");
        sourceDTO.setAuthor("Original Author");
        sourceDTO.setIsbn("9780140449112");

        BulkSourceRequest request = new BulkSourceRequest();
        request.setSources(java.util.Collections.singletonList(sourceDTO));

        Book googleBook = new Book();
        googleBook.setTitle("Google Book Title");
        googleBook.setAuthor("Google Author");
        googleBook.setPublisher("Google Publisher");
        googleBook.setPublicationYear(2023);

        when(googleBooksService.fetchBookDataByIsbn("9780140449112")).thenReturn(Mono.just(googleBook));
        when(bookRepository.findByTitleIgnoreCaseAndAuthorIgnoreCase(any(), any())).thenReturn(java.util.Optional.empty());
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book book = invocation.getArgument(0);
            book.setId(1L);
            return book;
        });
        when(submissionRepository.save(any(Submission.class))).thenAnswer(invocation -> {
            Submission submission = invocation.getArgument(0);
            submission.setId(1L);
            return submission;
        });
        when(citationRepository.save(any(Citation.class))).thenAnswer(invocation -> {
            Citation citation = invocation.getArgument(0);
            citation.setId(1L);
            return citation;
        });

        // Act
        sourceService.addOrAppendSources(request, null, true);

        // Assert
        verify(bookRepository).save(argThat(book ->
                "Original Title".equals(book.getTitle()) && // Should not be overwritten
                "Original Author".equals(book.getAuthor()) && // Should not be overwritten
                "Google Publisher".equals(book.getPublisher()) &&
                Integer.valueOf(2023).equals(book.getPublicationYear())
        ));
    }
}
