package com.columbia.coms4156.citationservice.controller;

import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.exception.ResourceNotFoundException;
import com.columbia.coms4156.citationservice.model.CitationResponse;
import com.columbia.coms4156.citationservice.model.ErrorResponse;
import com.columbia.coms4156.citationservice.model.GroupCitationResponse;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.service.CitationService;
import com.columbia.coms4156.citationservice.service.SourceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CitationController.class)
class CitationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CitationService citationService;

    @MockBean
    private SourceService sourceService;

    @Autowired
    private ObjectMapper objectMapper;

    // ==================== Book Endpoints ====================

    @Test
    @DisplayName("GET /api/cite/book/{id} returns citation for valid book ID")
    void generateBookCitation_Success() throws Exception {
        // Arrange
        Long bookId = 1L;
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        book.setId(bookId);
        String expectedCitation = "Fitzgerald, F. Scott. _The Great Gatsby_. ";

        given(sourceService.findBookById(bookId)).willReturn(Optional.of(book));
        given(citationService.generateMLACitation(any(Book.class))).willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(get("/api/cite/book/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }

    @Test
    @DisplayName("GET /api/cite/book/{id} returns 404 when book not found")
    void generateBookCitation_NotFound() throws Exception {
        // Arrange
        Long bookId = 999L;
        given(sourceService.findBookById(bookId)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/cite/book/{id}", bookId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No book found with ID: 999. "
                        + "Please verify the book ID and try again."))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path").exists());
    }

    @Test
    @DisplayName("GET /api/cite/book/{id} returns 400 for invalid ID")
    void generateBookCitation_InvalidId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/cite/book/{id}", 0))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("GET /api/cite/book/{id} returns 400 for negative ID")
    void generateBookCitation_NegativeId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/cite/book/{id}", -1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("GET /api/cite/book/{id} returns 400 for IllegalArgumentException")
    void generateBookCitation_IllegalArgumentException() throws Exception {
        // Arrange
        Long bookId = 1L;
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        book.setId(bookId);

        given(sourceService.findBookById(bookId)).willReturn(Optional.of(book));
        given(citationService.generateMLACitation(any(Book.class)))
                .willThrow(new IllegalArgumentException("Book data is invalid"));

        // Act & Assert
        mockMvc.perform(get("/api/cite/book/{id}", bookId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("GET /api/cite/book/{id} returns 500 for unexpected exception")
    void generateBookCitation_UnexpectedException() throws Exception {
        // Arrange
        Long bookId = 1L;
        given(sourceService.findBookById(bookId)).willThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/api/cite/book/{id}", bookId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    @DisplayName("POST /api/cite/book generates citation from book data")
    void generateBookCitationFromData_Success() throws Exception {
        // Arrange
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        String expectedCitation = "Fitzgerald, F. Scott. _The Great Gatsby_. ";

        given(citationService.generateCitationByStyle(any(Book.class), eq("MLA")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/book?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }

    @Test
    @DisplayName("POST /api/cite/book uses default MLA style when not specified")
    void generateBookCitationFromData_DefaultStyle() throws Exception {
        // Arrange
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        String expectedCitation = "Fitzgerald, F. Scott. _The Great Gatsby_. ";

        given(citationService.generateCitationByStyle(any(Book.class), eq("MLA")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/cite/book supports APA style")
    void generateBookCitationFromData_APAStyle() throws Exception {
        // Arrange
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        String expectedCitation = "Fitzgerald, F. S. (1925). The Great Gatsby. ";

        given(citationService.generateCitationByStyle(any(Book.class), eq("APA")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/book?style=APA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }

    @Test
    @DisplayName("POST /api/cite/book supports Chicago style")
    void generateBookCitationFromData_ChicagoStyle() throws Exception {
        // Arrange
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        String expectedCitation = "Fitzgerald, F. Scott. \"The Great Gatsby.\" ";

        given(citationService.generateCitationByStyle(any(Book.class), eq("CHICAGO")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/book?style=CHICAGO")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }

    @Test
    @DisplayName("POST /api/cite/book returns 400 for IllegalArgumentException")
    void generateBookCitationFromData_IllegalArgumentException() throws Exception {
        // Arrange
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald");

        given(citationService.generateCitationByStyle(any(Book.class), any(String.class)))
                .willThrow(new IllegalArgumentException("Invalid book data"));

        // Act & Assert
        mockMvc.perform(post("/api/cite/book?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }


    @Test
    @DisplayName("POST /api/cite/book returns 500 for unexpected exception")
    void generateBookCitationFromData_UnexpectedException() throws Exception {
        // Arrange
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald");

        given(citationService.generateCitationByStyle(any(Book.class), any(String.class)))
                .willThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        mockMvc.perform(post("/api/cite/book?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500));
    }

    // ==================== Video Endpoints ====================

    @Test
    @DisplayName("GET /api/cite/video/{id} returns citation for valid video ID")
    void generateVideoCitation_Success() throws Exception {
        // Arrange
        Long videoId = 2L;
        Video video = new Video("Introduction to Neural Networks", "3Blue1Brown");
        video.setId(videoId);
        String expectedCitation = "3Blue1Brown. _Introduction to Neural Networks_. ";

        given(sourceService.findVideoById(videoId)).willReturn(Optional.of(video));
        given(citationService.generateMLACitation(any(Video.class))).willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(get("/api/cite/video/{id}", videoId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }

    @Test
    @DisplayName("GET /api/cite/video/{id} returns 404 when video not found")
    void generateVideoCitation_NotFound() throws Exception {
        // Arrange
        Long videoId = 999L;
        given(sourceService.findVideoById(videoId)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/cite/video/{id}", videoId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /api/cite/video/{id} returns 400 for invalid ID")
    void generateVideoCitation_InvalidId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/cite/video/{id}", 0))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("GET /api/cite/video/{id} returns 500 for unexpected exception")
    void generateVideoCitation_UnexpectedException() throws Exception {
        // Arrange
        Long videoId = 2L;
        given(sourceService.findVideoById(videoId)).willThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/api/cite/video/{id}", videoId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    @DisplayName("POST /api/cite/video/citation generates citation from video data")
    void generateVideoCitationFromData_Success() throws Exception {
        // Arrange
        Video video = new Video("Introduction to Neural Networks", "3Blue1Brown");
        String expectedCitation = "3Blue1Brown. _Introduction to Neural Networks_. ";

        given(citationService.generateCitationByStyle(any(Video.class), eq("MLA")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/video/citation?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }

    @Test
    @DisplayName("POST /api/cite/video/citation supports APA style")
    void generateVideoCitationFromData_APAStyle() throws Exception {
        // Arrange
        Video video = new Video("Introduction to Neural Networks", "3Blue1Brown");
        String expectedCitation = "3Blue1Brown. (2020). Introduction to Neural Networks [Video]. ";

        given(citationService.generateCitationByStyle(any(Video.class), eq("APA")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/video/citation?style=APA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }


    // ==================== Article Endpoints ====================

    @Test
    @DisplayName("GET /api/cite/article/{id}/citation returns citation for valid article ID")
    void generateArticleCitation_Success() throws Exception {
        // Arrange
        Long articleId = 3L;
        Article article = new Article("Machine Learning Basics", "John Smith");
        article.setId(articleId);
        String expectedCitation = "Smith, John. \"Machine Learning Basics.\" ";

        given(sourceService.findArticleById(articleId)).willReturn(Optional.of(article));
        given(citationService.generateMLACitation(any(Article.class))).willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(get("/api/cite/article/{id}/citation", articleId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }

    @Test
    @DisplayName("GET /api/cite/article/{id}/citation returns 404 when article not found")
    void generateArticleCitation_NotFound() throws Exception {
        // Arrange
        Long articleId = 999L;
        given(sourceService.findArticleById(articleId)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/cite/article/{id}/citation", articleId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /api/cite/article/{id}/citation returns 400 for invalid ID")
    void generateArticleCitation_InvalidId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/cite/article/{id}/citation", 0))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("POST /api/cite/article/citation generates citation from article data")
    void generateArticleCitationFromData_Success() throws Exception {
        // Arrange
        Article article = new Article("Machine Learning Basics", "John Smith");
        String expectedCitation = "Smith, John. \"Machine Learning Basics.\" ";

        given(citationService.generateCitationByStyle(any(Article.class), eq("MLA")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/article/citation?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }

    @Test
    @DisplayName("POST /api/cite/article/citation supports APA style")
    void generateArticleCitationFromData_APAStyle() throws Exception {
        // Arrange
        Article article = new Article("Machine Learning Basics", "John Smith");
        String expectedCitation = "Smith, J. (2023). Machine Learning Basics. ";

        given(citationService.generateCitationByStyle(any(Article.class), eq("APA")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/article/citation?style=APA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }


    // ==================== General Use Endpoints ====================

    @Test
    @DisplayName("GET /api/cite/source/{sourceId} returns citation for valid source")
    void generateCitationForSource_Success() throws Exception {
        // Arrange
        Long sourceId = 10L;
        CitationResponse response = new CitationResponse("10", "Sample citation text");
        String defaultStyle = "MLA";

        given(citationService.generateCitationForSource(eq(sourceId), eq(defaultStyle), eq(false)))
                .willReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/cite/source/{sourceId}", sourceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.CitationID").value("10"))
                .andExpect(jsonPath("$.CitationString").value("Sample citation text"));
    }

    @Test
    @DisplayName("GET /api/cite/source/{sourceId} supports APA style")
    void generateCitationForSource_APAStyle() throws Exception {
        // Arrange
        Long sourceId = 10L;
        CitationResponse response = new CitationResponse("10", "APA citation text");

        given(citationService.generateCitationForSource(eq(sourceId), eq("APA"), eq(false)))
                .willReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/cite/source/{sourceId}?style=APA", sourceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.CitationString").value("APA citation text"));
    }

    @Test
    @DisplayName("GET /api/cite/source/{sourceId} returns 404 when source not found")
    void generateCitationForSource_NotFound() throws Exception {
        // Arrange
        Long sourceId = 999L;
        String defaultStyle = "MLA";

        given(citationService.generateCitationForSource(eq(sourceId), eq(defaultStyle), eq(false)))
                .willThrow(new ResourceNotFoundException("Citation not found with ID: " + sourceId));

        // Act & Assert
        mockMvc.perform(get("/api/cite/source/{sourceId}", sourceId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /api/cite/source/{sourceId} returns 400 for invalid ID")
    void generateCitationForSource_InvalidId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/cite/source/{sourceId}", 0))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }


    @Test
    @DisplayName("GET /api/cite/source/{sourceId} returns 500 for unexpected exception")
    void generateCitationForSource_UnexpectedException() throws Exception {
        // Arrange
        Long sourceId = 10L;
        String defaultStyle = "MLA";

        given(citationService.generateCitationForSource(eq(sourceId), eq(defaultStyle), eq(false)))
                .willThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        mockMvc.perform(get("/api/cite/source/{sourceId}", sourceId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    @DisplayName("GET /api/cite/group/{submissionId} returns citations for valid submission")
    void generateCitationsForGroup_Success() throws Exception {
        // Arrange
        Long submissionId = 5L;
        Map<String, String> citations = new HashMap<>();
        citations.put("1", "Book citation");
        citations.put("2", "Video citation");
        GroupCitationResponse response = new GroupCitationResponse(submissionId, citations);
        String defaultStyle = "MLA";

        given(citationService.generateCitationsForGroup(eq(submissionId), eq(defaultStyle), eq(false)))
                .willReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/cite/group/{submissionId}", submissionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.submissionId").value(5))
                .andExpect(jsonPath("$.Citations").exists())
                .andExpect(jsonPath("$.Citations['1']").value("Book citation"))
                .andExpect(jsonPath("$.Citations['2']").value("Video citation"));
    }

    @Test
    @DisplayName("GET /api/cite/group/{submissionId} supports APA style")
    void generateCitationsForGroup_APAStyle() throws Exception {
        // Arrange
        Long submissionId = 5L;
        Map<String, String> citations = new HashMap<>();
        citations.put("1", "APA Book citation");
        GroupCitationResponse response = new GroupCitationResponse(submissionId, citations);

        given(citationService.generateCitationsForGroup(eq(submissionId), eq("APA"), eq(false)))
                .willReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/cite/group/{submissionId}?style=APA", submissionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Citations['1']").value("APA Book citation"));
    }

    @Test
    @DisplayName("GET /api/cite/group/{submissionId} returns 404 when submission not found")
    void generateCitationsForGroup_NotFound() throws Exception {
        // Arrange
        Long submissionId = 999L;
        String defaultStyle = "MLA";

        given(citationService.generateCitationsForGroup(eq(submissionId), eq(defaultStyle), eq(false)))
                .willThrow(new ResourceNotFoundException("Submission not found with ID: " + submissionId));

        // Act & Assert
        mockMvc.perform(get("/api/cite/group/{submissionId}", submissionId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /api/cite/group/{submissionId} returns 400 for invalid ID")
    void generateCitationsForGroup_InvalidId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/cite/group/{submissionId}", 0))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }


    @Test
    @DisplayName("GET /api/cite/group/{submissionId} returns 500 for unexpected exception")
    void generateCitationsForGroup_UnexpectedException() throws Exception {
        // Arrange
        Long submissionId = 5L;
        String defaultStyle = "MLA";

        given(citationService.generateCitationsForGroup(eq(submissionId), eq(defaultStyle), eq(false)))
                .willThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        mockMvc.perform(get("/api/cite/group/{submissionId}", submissionId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    @DisplayName("GET /api/cite/source/{sourceId} supports backfill parameter")
    void generateCitationForSource_WithBackfill() throws Exception {
        // Arrange
        Long sourceId = 10L;
        CitationResponse response = new CitationResponse("10", "Citation with backfill");

        given(citationService.generateCitationForSource(eq(sourceId), eq("MLA"), eq(true)))
                .willReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/cite/source/{sourceId}?backfill=true", sourceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.CitationString").value("Citation with backfill"));
    }

    @Test
    @DisplayName("GET /api/cite/group/{submissionId} supports backfill parameter")
    void generateCitationsForGroup_WithBackfill() throws Exception {
        // Arrange
        Long submissionId = 5L;
        Map<String, String> citations = new HashMap<>();
        citations.put("1", "Citation with backfill");
        GroupCitationResponse response = new GroupCitationResponse(submissionId, citations);

        given(citationService.generateCitationsForGroup(eq(submissionId), eq("MLA"), eq(true)))
                .willReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/cite/group/{submissionId}?backfill=true", submissionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.submissionId").value(5))
                .andExpect(jsonPath("$.Citations['1']").value("Citation with backfill"));
    }
}

