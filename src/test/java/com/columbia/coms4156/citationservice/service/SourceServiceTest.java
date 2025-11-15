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

import com.columbia.coms4156.citationservice.controller.dto.UserDTO;
import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.model.User;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.repository.ArticleRepository;
import com.columbia.coms4156.citationservice.repository.UserRepository;
import com.columbia.coms4156.citationservice.repository.VideoRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SourceServiceTest {

    @InjectMocks
    private SourceService sourceService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private UserRepository userRepository;

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
    void testAddOrAppendSources_NewSubmission() {
        SourceDTO sourceDTO = new SourceDTO();
        sourceDTO.setMediaType("book");
        sourceDTO.setTitle("Test Book");
        sourceDTO.setAuthor("Test Author");

        BulkSourceRequest request = new BulkSourceRequest();
        request.setSources(Collections.singletonList(sourceDTO));

        Submission submission = new Submission();
        submission.setId(1L);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");

        Citation citation = new Citation();
        citation.setId(1L);

        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);
        when(bookRepository.findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(citationRepository.save(any(Citation.class))).thenReturn(citation);

        var response = sourceService.addOrAppendSources(request, null);

        assertEquals(1L, response.getSubmissionId());
        assertEquals(1, response.getSourceIds().size());
        assertEquals("1", response.getSourceIds().get(0));

        verify(submissionRepository, times(1)).save(any(Submission.class));
        verify(bookRepository, times(1)).findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(citationRepository, times(1)).save(any(Citation.class));
    }

    @Test
    void testAddOrAppendSources_ExistingSubmission() {
        SourceDTO sourceDTO = new SourceDTO();
        sourceDTO.setMediaType("book");
        sourceDTO.setTitle("Test Book");
        sourceDTO.setAuthor("Test Author");

        BulkSourceRequest request = new BulkSourceRequest();
        request.setSources(Collections.singletonList(sourceDTO));

        Submission submission = new Submission();
        submission.setId(1L);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");

        Citation citation = new Citation();
        citation.setId(1L);

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));
        when(bookRepository.findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(citationRepository.save(any(Citation.class))).thenReturn(citation);

        var response = sourceService.addOrAppendSources(request, 1L);

        assertEquals(1L, response.getSubmissionId());
        assertEquals(1, response.getSourceIds().size());
        assertEquals("1", response.getSourceIds().get(0));

        verify(submissionRepository, never()).save(any(Submission.class));
        verify(submissionRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(citationRepository, times(1)).save(any(Citation.class));
    }

    @Test
    void testAddOrAppendSources_EmptySourceList() {
        BulkSourceRequest request = new BulkSourceRequest();
        request.setSources(Collections.emptyList());

        var response = sourceService.addOrAppendSources(request, null);

        assertEquals(null, response.getSubmissionId());
        assertTrue(response.getSourceIds().isEmpty());

        verify(submissionRepository, never()).save(any(Submission.class));
        verify(bookRepository, never()).findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString());
        verify(bookRepository, never()).save(any(Book.class));
        verify(citationRepository, never()).save(any(Citation.class));
    }

    @Test
    void testAddOrAppendSources_UnsupportedMediaType() {
        SourceDTO sourceDTO = new SourceDTO();
        sourceDTO.setMediaType("unsupported");
        sourceDTO.setTitle("Test Unsupported");
        sourceDTO.setAuthor("Test Author");

        BulkSourceRequest request = new BulkSourceRequest();
        request.setSources(Collections.singletonList(sourceDTO));

        Submission submission = new Submission();
        submission.setId(1L);

        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);

        var response = sourceService.addOrAppendSources(request, null);

        assertEquals(1L, response.getSubmissionId());
        assertTrue(response.getSourceIds().isEmpty());
        assertFalse(response.getErrors().isEmpty());

        verify(submissionRepository, times(1)).save(any(Submission.class));
        verify(bookRepository, never()).findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString());
        verify(bookRepository, never()).save(any(Book.class));
        verify(citationRepository, never()).save(any(Citation.class));
    }

    @Test
    void testAddOrAppendSources_MixedMediaTypes() {
        SourceDTO bookDTO = new SourceDTO();
        bookDTO.setMediaType("book");
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");

        SourceDTO unsupportedDTO = new SourceDTO();
        unsupportedDTO.setMediaType("unsupported");
        unsupportedDTO.setTitle("Test Unsupported");
        unsupportedDTO.setAuthor("Test Author");

        BulkSourceRequest request = new BulkSourceRequest();
        request.setSources(List.of(bookDTO, unsupportedDTO));

        Submission submission = new Submission();
        submission.setId(1L);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");

        Citation citation = new Citation();
        citation.setId(1L);

        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);
        when(bookRepository.findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(citationRepository.save(any(Citation.class))).thenReturn(citation);

        var response = sourceService.addOrAppendSources(request, null);

        assertEquals(1L, response.getSubmissionId());
        assertEquals(1, response.getSourceIds().size());
        assertEquals("1", response.getSourceIds().get(0));
        assertFalse(response.getErrors().isEmpty());

        verify(submissionRepository, times(1)).save(any(Submission.class));
        verify(bookRepository, times(1)).findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(citationRepository, times(1)).save(any(Citation.class));
    }

    @Test
    void testAddOrAppendSources_BookAlreadyExists() {
        SourceDTO sourceDTO = new SourceDTO();
        sourceDTO.setMediaType("book");
        sourceDTO.setTitle("Test Book");
        sourceDTO.setAuthor("Test Author");

        BulkSourceRequest request = new BulkSourceRequest();
        request.setSources(Collections.singletonList(sourceDTO));

        Submission submission = new Submission();
        submission.setId(1L);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");

        Citation citation = new Citation();
        citation.setId(1L);

        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);
        when(bookRepository.findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(book));
        when(citationRepository.save(any(Citation.class))).thenReturn(citation);

        var response = sourceService.addOrAppendSources(request, null);

        assertEquals(1L, response.getSubmissionId());
        assertEquals(1, response.getSourceIds().size());
        assertEquals("1", response.getSourceIds().get(0));

        verify(submissionRepository, times(1)).save(any(Submission.class));
        verify(bookRepository, times(1)).findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString());
        verify(bookRepository, never()).save(any(Book.class));
        verify(citationRepository, times(1)).save(any(Citation.class));
    }

    @Test
    void testAddOrAppendSources_ArticleAlreadyExists() {
        SourceDTO sourceDTO = new SourceDTO();
        sourceDTO.setMediaType("article");
        sourceDTO.setTitle("Test Article");
        sourceDTO.setAuthor("Test Author");

        BulkSourceRequest request = new BulkSourceRequest();
        request.setSources(Collections.singletonList(sourceDTO));

        Submission submission = new Submission();
        submission.setId(1L);

        Article article = new Article();
        article.setId(1L);
        article.setTitle("Test Article");
        article.setAuthor("Test Author");

        Citation citation = new Citation();
        citation.setId(1L);

        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);
        when(articleRepository.findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(article));
        when(citationRepository.save(any(Citation.class))).thenReturn(citation);

        var response = sourceService.addOrAppendSources(request, null);

        assertEquals(1L, response.getSubmissionId());
        assertEquals(1, response.getSourceIds().size());
        assertEquals("1", response.getSourceIds().get(0));

        verify(submissionRepository, times(1)).save(any(Submission.class));
        verify(articleRepository, times(1)).findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString());
        verify(articleRepository, never()).save(any(Article.class));
        verify(citationRepository, times(1)).save(any(Citation.class));
    }

    @Test
    void testAddOrAppendSources_VideoAlreadyExists() {
        SourceDTO sourceDTO = new SourceDTO();
        sourceDTO.setMediaType("video");
        sourceDTO.setTitle("Test Video");
        sourceDTO.setAuthor("Test Author");

        BulkSourceRequest request = new BulkSourceRequest();
        request.setSources(Collections.singletonList(sourceDTO));

        Submission submission = new Submission();
        submission.setId(1L);

        Video video = new Video();
        video.setId(1L);
        video.setTitle("Test Video");
        video.setAuthor("Test Author");

        Citation citation = new Citation();
        citation.setId(1L);

        when(submissionRepository.save(any(Submission.class))).thenReturn(submission);
        when(videoRepository.findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString())).thenReturn(Optional.of(video));
        when(citationRepository.save(any(Citation.class))).thenReturn(citation);

        var response = sourceService.addOrAppendSources(request, null);

        assertEquals(1L, response.getSubmissionId());
        assertEquals(1, response.getSourceIds().size());
        assertEquals("1", response.getSourceIds().get(0));

        verify(submissionRepository, times(1)).save(any(Submission.class));
        verify(videoRepository, times(1)).findByTitleIgnoreCaseAndAuthorIgnoreCase(anyString(), anyString());
        verify(videoRepository, never()).save(any(Video.class));
        verify(citationRepository, times(1)).save(any(Citation.class));
    }
}

