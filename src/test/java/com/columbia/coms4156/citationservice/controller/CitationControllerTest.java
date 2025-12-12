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
    @DisplayName("POST /api/cite/video generates citation from video data")
    void generateVideoCitationFromData_Success() throws Exception {
        // Arrange
        Video video = new Video("Introduction to Neural Networks", "3Blue1Brown");
        String expectedCitation = "3Blue1Brown. _Introduction to Neural Networks_. ";

        given(citationService.generateCitationByStyle(any(Video.class), eq("MLA")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/video?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }

    @Test
    @DisplayName("POST /api/cite/video supports APA style")
    void generateVideoCitationFromData_APAStyle() throws Exception {
        // Arrange
        Video video = new Video("Introduction to Neural Networks", "3Blue1Brown");
        String expectedCitation = "3Blue1Brown. (2020). Introduction to Neural Networks [Video]. ";

        given(citationService.generateCitationByStyle(any(Video.class), eq("APA")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/video?style=APA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }


    // ==================== Article Endpoints ====================

    @Test
    @DisplayName("GET /api/cite/article/{id} returns citation for valid article ID")
    void generateArticleCitation_Success() throws Exception {
        // Arrange
        Long articleId = 3L;
        Article article = new Article("Machine Learning Basics", "John Smith");
        article.setId(articleId);
        String expectedCitation = "Smith, John. \"Machine Learning Basics.\" ";

        given(sourceService.findArticleById(articleId)).willReturn(Optional.of(article));
        given(citationService.generateMLACitation(any(Article.class))).willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(get("/api/cite/article/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }

    @Test
    @DisplayName("GET /api/cite/article/{id} returns 404 when article not found")
    void generateArticleCitation_NotFound() throws Exception {
        // Arrange
        Long articleId = 999L;
        given(sourceService.findArticleById(articleId)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/cite/article/{id}", articleId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /api/cite/article/{id} returns 400 for invalid ID")
    void generateArticleCitation_InvalidId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/cite/article/{id}", 0))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("POST /api/cite/article generates citation from article data")
    void generateArticleCitationFromData_Success() throws Exception {
        // Arrange
        Article article = new Article("Machine Learning Basics", "John Smith");
        String expectedCitation = "Smith, John. \"Machine Learning Basics.\" ";

        given(citationService.generateCitationByStyle(any(Article.class), eq("MLA")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/article?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }

    @Test
    @DisplayName("POST /api/cite/article supports APA style")
    void generateArticleCitationFromData_APAStyle() throws Exception {
        // Arrange
        Article article = new Article("Machine Learning Basics", "John Smith");
        String expectedCitation = "Smith, J. (2023). Machine Learning Basics. ";

        given(citationService.generateCitationByStyle(any(Article.class), eq("APA")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/article?style=APA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }


    // ==================== General Use Endpoints ====================

    @Test
    @DisplayName("GET /api/cite/{citationId} returns citation for valid source")
    void generateCitationForSource_Success() throws Exception {
        // Arrange
        Long citationId = 10L;
        CitationResponse response = new CitationResponse("10", "Sample citation text");
        String defaultStyle = "MLA";

        given(citationService.generateCitationForSource(eq(citationId), eq(defaultStyle), eq(false)))
                .willReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/cite/{citationId}", citationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.CitationID").value("10"))
                .andExpect(jsonPath("$.CitationString").value("Sample citation text"));
    }

    @Test
    @DisplayName("GET /api/cite/{citationId} supports APA style")
    void generateCitationForSource_APAStyle() throws Exception {
        // Arrange
        Long citationId = 10L;
        CitationResponse response = new CitationResponse("10", "APA citation text");

        given(citationService.generateCitationForSource(eq(citationId), eq("APA"), eq(false)))
                .willReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/cite/{citationId}?style=APA", citationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.CitationString").value("APA citation text"));
    }

    @Test
    @DisplayName("GET /api/cite/{citationId} returns 404 when source not found")
    void generateCitationForSource_NotFound() throws Exception {
        // Arrange
        Long citationId = 999L;
        String defaultStyle = "MLA";

        given(citationService.generateCitationForSource(eq(citationId), eq(defaultStyle), eq(false)))
                .willThrow(new ResourceNotFoundException("Citation not found with ID: " + citationId));

        // Act & Assert
        mockMvc.perform(get("/api/cite/{citationId}", citationId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("GET /api/cite/{citationId} returns 400 for invalid ID")
    void generateCitationForSource_InvalidId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/cite/{citationId}", 0))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("GET /api/cite/{citationId} returns 400 for negative ID")
    void generateCitationForSource_NegativeId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/cite/{citationId}", -1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }


    @Test
    @DisplayName("GET /api/cite/{citationId} returns 500 for unexpected exception")
    void generateCitationForSource_UnexpectedException() throws Exception {
        // Arrange
        Long citationId = 10L;
        String defaultStyle = "MLA";

        given(citationService.generateCitationForSource(eq(citationId), eq(defaultStyle), eq(false)))
                .willThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        mockMvc.perform(get("/api/cite/{citationId}", citationId))
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
    @DisplayName("GET /api/cite/group/{submissionId} returns 400 for negative ID")
    void generateCitationsForGroup_NegativeId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/cite/group/{submissionId}", -1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("POST /api/cite/book returns 400 for invalid JSON")
    void generateBookCitationFromData_InvalidJson() throws Exception {
        // Act & Assert - testing GlobalExceptionHandler for invalid JSON
        mockMvc.perform(post("/api/cite/book?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("GET /api/cite/{citationId} returns 400 for invalid ID type")
    void generateCitationForSource_InvalidIdType() throws Exception {
        // Act & Assert - testing GlobalExceptionHandler branch where requiredType is null (line 155)
        // Passing invalid ID type (non-numeric) should trigger MethodArgumentTypeMismatchException
        mockMvc.perform(get("/api/cite/{citationId}", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
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
    @DisplayName("GET /api/cite/{citationId} supports backfill parameter")
    void generateCitationForSource_WithBackfill() throws Exception {
        // Arrange
        Long citationId = 10L;
        CitationResponse response = new CitationResponse("10", "Citation with backfill");

        given(citationService.generateCitationForSource(eq(citationId), eq("MLA"), eq(true)))
                .willReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/cite/{citationId}?backfill=true", citationId))
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

    // ==================== Additional Branch Coverage Tests ====================

    @Test
    @DisplayName("POST /api/cite/book returns 400 for empty style")
    void generateBookCitationFromData_EmptyStyle() throws Exception {
        // Arrange
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald");

        // Act & Assert - Spring will use default "MLA" for empty string, so we test whitespace
        mockMvc.perform(post("/api/cite/book?style=   ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("POST /api/cite/book returns 400 for whitespace-only style")
    void generateBookCitationFromData_WhitespaceStyle() throws Exception {
        // Arrange
        Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald");

        // Act & Assert
        mockMvc.perform(post("/api/cite/book?style=   ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("POST /api/cite/video supports Chicago style")
    void generateVideoCitationFromData_ChicagoStyle() throws Exception {
        // Arrange
        Video video = new Video("Introduction to Neural Networks", "3Blue1Brown");
        String expectedCitation = "3Blue1Brown. \"Introduction to Neural Networks.\" ";

        given(citationService.generateCitationByStyle(any(Video.class), eq("CHICAGO")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/video?style=CHICAGO")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }

    @Test
    @DisplayName("POST /api/cite/video returns 400 for empty style")
    void generateVideoCitationFromData_EmptyStyle() throws Exception {
        // Arrange
        Video video = new Video("Introduction to Neural Networks", "3Blue1Brown");

        // Act & Assert - Spring will use default "MLA" for empty string, so we test whitespace
        mockMvc.perform(post("/api/cite/video?style=   ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("POST /api/cite/article supports Chicago style")
    void generateArticleCitationFromData_ChicagoStyle() throws Exception {
        // Arrange
        Article article = new Article("Machine Learning Basics", "John Smith");
        String expectedCitation = "Smith, John. \"Machine Learning Basics.\" ";

        given(citationService.generateCitationByStyle(any(Article.class), eq("CHICAGO")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/article?style=CHICAGO")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCitation));
    }

    @Test
    @DisplayName("POST /api/cite/article returns 400 for empty style")
    void generateArticleCitationFromData_EmptyStyle() throws Exception {
        // Arrange
        Article article = new Article("Machine Learning Basics", "John Smith");

        // Act & Assert - Spring will use default "MLA" for empty string, so we test whitespace
        mockMvc.perform(post("/api/cite/article?style=   ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("GET /api/cite/{citationId} returns 400 for empty style")
    void generateCitationForSource_EmptyStyle() throws Exception {
        // Act & Assert - Spring will use default "MLA" for empty string, so we test whitespace
        mockMvc.perform(get("/api/cite/{citationId}?style=   ", 10))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("GET /api/cite/{citationId} supports Chicago style")
    void generateCitationForSource_ChicagoStyle() throws Exception {
        // Arrange
        Long citationId = 10L;
        CitationResponse response = new CitationResponse("10", "Chicago citation text");

        given(citationService.generateCitationForSource(eq(citationId), eq("CHICAGO"), eq(false)))
                .willReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/cite/{citationId}?style=CHICAGO", citationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.CitationString").value("Chicago citation text"));
    }

    @Test
    @DisplayName("GET /api/cite/group/{submissionId} returns 400 for empty style")
    void generateCitationsForGroup_EmptyStyle() throws Exception {
        // Act & Assert - Spring will use default "MLA" for empty string, so we test whitespace
        mockMvc.perform(get("/api/cite/group/{submissionId}?style=   ", 5))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("GET /api/cite/group/{submissionId} supports Chicago style")
    void generateCitationsForGroup_ChicagoStyle() throws Exception {
        // Arrange
        Long submissionId = 5L;
        Map<String, String> citations = new HashMap<>();
        citations.put("1", "Chicago Book citation");
        GroupCitationResponse response = new GroupCitationResponse(submissionId, citations);

        given(citationService.generateCitationsForGroup(eq(submissionId), eq("CHICAGO"), eq(false)))
                .willReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/cite/group/{submissionId}?style=CHICAGO", submissionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Citations['1']").value("Chicago Book citation"));
    }

    @Test
    @DisplayName("POST /api/cite/book returns 400 for MethodArgumentNotValidException (missing title)")
    void generateBookCitationFromData_MethodArgumentNotValidException_EmptyTitle() throws Exception {
        // Arrange - Book with missing title field to trigger @NotBlank validation
        // Using a Map to create JSON without title field
        Map<String, String> bookMap = new HashMap<>();
        bookMap.put("author", "F. Scott Fitzgerald");
        String jsonWithoutTitle = objectMapper.writeValueAsString(bookMap);

        // Act & Assert
        mockMvc.perform(post("/api/cite/book?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithoutTitle))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Validation failed")));
    }

    @Test
    @DisplayName("POST /api/cite/book returns 400 for MethodArgumentNotValidException (missing author)")
    void generateBookCitationFromData_MethodArgumentNotValidException_EmptyAuthor() throws Exception {
        // Arrange - Book with missing author field to trigger @NotBlank validation
        Map<String, String> bookMap = new HashMap<>();
        bookMap.put("title", "The Great Gatsby");
        String jsonWithoutAuthor = objectMapper.writeValueAsString(bookMap);

        // Act & Assert
        mockMvc.perform(post("/api/cite/book?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithoutAuthor))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Validation failed")));
    }

    @Test
    @DisplayName("POST /api/cite/video returns 400 for MethodArgumentNotValidException (missing title)")
    void generateVideoCitationFromData_MethodArgumentNotValidException_EmptyTitle() throws Exception {
        // Arrange - Video with missing title field to trigger @NotBlank validation
        Map<String, String> videoMap = new HashMap<>();
        videoMap.put("author", "3Blue1Brown");
        String jsonWithoutTitle = objectMapper.writeValueAsString(videoMap);

        // Act & Assert
        mockMvc.perform(post("/api/cite/video?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithoutTitle))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Validation failed")));
    }

    @Test
    @DisplayName("POST /api/cite/article returns 400 for MethodArgumentNotValidException (missing title)")
    void generateArticleCitationFromData_MethodArgumentNotValidException_EmptyTitle() throws Exception {
        // Arrange - Article with missing title field to trigger @NotBlank validation
        Map<String, String> articleMap = new HashMap<>();
        articleMap.put("author", "John Smith");
        String jsonWithoutTitle = objectMapper.writeValueAsString(articleMap);

        // Act & Assert
        mockMvc.perform(post("/api/cite/article?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithoutTitle))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Validation failed")));
    }

    @Test
    @DisplayName("GET /api/cite/video/{id} returns 400 for negative ID")
    void generateVideoCitation_NegativeId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/cite/video/{id}", -1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("GET /api/cite/article/{id} returns 400 for negative ID")
    void generateArticleCitation_NegativeId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/cite/article/{id}", -1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("GET /api/cite/video/{id} returns 400 for IllegalArgumentException")
    void generateVideoCitation_IllegalArgumentException() throws Exception {
        // Arrange
        Long videoId = 2L;
        Video video = new Video("Introduction to Neural Networks", "3Blue1Brown");
        video.setId(videoId);

        given(sourceService.findVideoById(videoId)).willReturn(Optional.of(video));
        given(citationService.generateMLACitation(any(Video.class)))
                .willThrow(new IllegalArgumentException("Video data is invalid"));

        // Act & Assert
        mockMvc.perform(get("/api/cite/video/{id}", videoId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("GET /api/cite/article/{id} returns 400 for IllegalArgumentException")
    void generateArticleCitation_IllegalArgumentException() throws Exception {
        // Arrange
        Long articleId = 3L;
        Article article = new Article("Machine Learning Basics", "John Smith");
        article.setId(articleId);

        given(sourceService.findArticleById(articleId)).willReturn(Optional.of(article));
        given(citationService.generateMLACitation(any(Article.class)))
                .willThrow(new IllegalArgumentException("Article data is invalid"));

        // Act & Assert
        mockMvc.perform(get("/api/cite/article/{id}", articleId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("POST /api/cite/video returns 400 for IllegalArgumentException")
    void generateVideoCitationFromData_IllegalArgumentException() throws Exception {
        // Arrange
        Video video = new Video("Introduction to Neural Networks", "3Blue1Brown");

        given(citationService.generateCitationByStyle(any(Video.class), any(String.class)))
                .willThrow(new IllegalArgumentException("Invalid video data"));

        // Act & Assert
        mockMvc.perform(post("/api/cite/video?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("POST /api/cite/article returns 400 for IllegalArgumentException")
    void generateArticleCitationFromData_IllegalArgumentException() throws Exception {
        // Arrange
        Article article = new Article("Machine Learning Basics", "John Smith");

        given(citationService.generateCitationByStyle(any(Article.class), any(String.class)))
                .willThrow(new IllegalArgumentException("Invalid article data"));

        // Act & Assert
        mockMvc.perform(post("/api/cite/article?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("POST /api/cite/article returns 500 for unexpected exception")
    void generateArticleCitationFromData_UnexpectedException() throws Exception {
        // Arrange
        Article article = new Article("Machine Learning Basics", "John Smith");

        given(citationService.generateCitationByStyle(any(Article.class), any(String.class)))
                .willThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        mockMvc.perform(post("/api/cite/article?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    @DisplayName("GET /api/cite/article/{id} returns 500 for unexpected exception")
    void generateArticleCitation_UnexpectedException() throws Exception {
        // Arrange
        Long articleId = 3L;
        given(sourceService.findArticleById(articleId)).willThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(get("/api/cite/article/{id}", articleId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    @DisplayName("POST /api/cite/video returns 500 for unexpected exception")
    void generateVideoCitationFromData_UnexpectedException() throws Exception {
        // Arrange
        Video video = new Video("Introduction to Neural Networks", "3Blue1Brown");

        given(citationService.generateCitationByStyle(any(Video.class), any(String.class)))
                .willThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        mockMvc.perform(post("/api/cite/video?style=MLA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    @DisplayName("POST /api/cite/video uses default MLA style when not specified")
    void generateVideoCitationFromData_DefaultStyle() throws Exception {
        // Arrange
        Video video = new Video("Introduction to Neural Networks", "3Blue1Brown");
        String expectedCitation = "3Blue1Brown. _Introduction to Neural Networks_. ";

        given(citationService.generateCitationByStyle(any(Video.class), eq("MLA")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/video")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/cite/article uses default MLA style when not specified")
    void generateArticleCitationFromData_DefaultStyle() throws Exception {
        // Arrange
        Article article = new Article("Machine Learning Basics", "John Smith");
        String expectedCitation = "Smith, John. \"Machine Learning Basics.\" ";

        given(citationService.generateCitationByStyle(any(Article.class), eq("MLA")))
                .willReturn(expectedCitation);

        // Act & Assert
        mockMvc.perform(post("/api/cite/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isOk());
    }
}

