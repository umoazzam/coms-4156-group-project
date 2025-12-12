package com.columbia.coms4156.citationservice.controller;

import com.columbia.coms4156.citationservice.controller.dto.BulkSourceRequest;
import com.columbia.coms4156.citationservice.controller.dto.SourceBatchResponse;
import com.columbia.coms4156.citationservice.controller.dto.SourceDTO;
import com.columbia.coms4156.citationservice.controller.dto.UserDTO;
import com.columbia.coms4156.citationservice.exception.ResourceNotFoundException;
import com.columbia.coms4156.citationservice.exception.ValidationException;
import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.service.SourceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(SourceController.class)
class SourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SourceService sourceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/source/book/{id} returns 400 for type mismatch (invalid ID)")
    void getBookById_TypeMismatch() throws Exception {
        mockMvc.perform(get("/api/source/book/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(
                        "Invalid parameter 'id': Value 'abc' could not be converted to type Long."));
    }

    @Test
    @DisplayName("POST /api/source/book returns 400 for malformed JSON")
    void createBook_MalformedJSON() throws Exception {
        String malformedJson = "{\"title\": \"The Great Gatsby\", \"author\": \"F. Scott Fitzgerald\""; // Missing closing brace

        mockMvc.perform(post("/api/source/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Malformed JSON request")));
    }

    @Test
    @DisplayName("POST /api/source/book/{id} returns 405 Method Not Allowed")
    void updateBook_MethodNotAllowed() throws Exception {
        mockMvc.perform(post("/api/source/book/1"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.error").value("Method Not Allowed"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Request method 'POST' is not supported")));
    }

    @Test
    @DisplayName("POST /api/source/book returns 415 Unsupported Media Type")
    void createBook_UnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/api/source/book")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Some text content"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.error").value("Unsupported Media Type"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Content-Type 'text/plain;charset=UTF-8' is not supported")));
    }

    @Test
    @DisplayName("POST /api/source/book successfully creates a new book")
    void createBook_Success() throws Exception {
        Book inputBook = new Book("Clean Code", "Robert C. Martin");
        inputBook.setPublisher("Prentice Hall");
        inputBook.setPublicationYear(2008);

        Book savedBook = new Book("Clean Code", "Robert C. Martin");
        savedBook.setId(1L);
        savedBook.setPublisher("Prentice Hall");
        savedBook.setPublicationYear(2008);

        given(sourceService.saveBook(any(Book.class))).willReturn(savedBook);

        mockMvc.perform(post("/api/source/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"));
    }

    @Test
    @DisplayName("POST /api/source/book returns 400 when book is null")
    void createBook_NullBook() throws Exception {
        mockMvc.perform(post("/api/source/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/source/video successfully creates a new video")
    void createVideo_Success() throws Exception {
        Video inputVideo = new Video("Understanding Neural Networks", "3Blue1Brown");
        inputVideo.setPlatform("YouTube");
        inputVideo.setReleaseYear(2017);

        Video savedVideo = new Video("Understanding Neural Networks", "3Blue1Brown");
        savedVideo.setId(1L);
        savedVideo.setPlatform("YouTube");
        savedVideo.setReleaseYear(2017);

        given(sourceService.saveVideo(any(Video.class))).willReturn(savedVideo);

        mockMvc.perform(post("/api/source/video")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputVideo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Understanding Neural Networks"))
                .andExpect(jsonPath("$.author").value("3Blue1Brown"));
    }

    @Test
    @DisplayName("POST /api/source/video returns 400 when video is null")
    void createVideo_NullVideo() throws Exception {
        mockMvc.perform(post("/api/source/video")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }

    // --- Create Article Tests ---
    @Test
    @DisplayName("POST /api/source/article successfully creates a new article")
    void createArticle_Success() throws Exception {
        Article inputArticle = new Article("Deep Learning", "Yann LeCun");
        inputArticle.setJournal("Nature");
        inputArticle.setPublicationYear(2015);
        inputArticle.setDoi("10.1038/nature14539");

        Article savedArticle = new Article("Deep Learning", "Yann LeCun");
        savedArticle.setId(1L);
        savedArticle.setJournal("Nature");
        savedArticle.setPublicationYear(2015);
        savedArticle.setDoi("10.1038/nature14539");

        given(sourceService.saveArticle(any(Article.class))).willReturn(savedArticle);

        mockMvc.perform(post("/api/source/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputArticle)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Deep Learning"))
                .andExpect(jsonPath("$.author").value("Yann LeCun"));
    }

    @Test
    @DisplayName("POST /api/source/article returns 400 when article is null")
    void createArticle_NullArticle() throws Exception {
        mockMvc.perform(post("/api/source/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
  @DisplayName("PUT /api/source/book/{id} successfully updates a book")
  void updateBook_Success() throws Exception {
    Long bookId = 1L;
    Book updatedBook = new Book("Clean Code Updated", "Robert C. Martin");
    updatedBook.setId(bookId);
    updatedBook.setPublisher("Prentice Hall");

    given(sourceService.updateBook(eq(bookId), any(Book.class))).willReturn(updatedBook);

    mockMvc.perform(put("/api/source/book/" + bookId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedBook)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(bookId))
            .andExpect(jsonPath("$.title").value("Clean Code Updated"));
  }

    @Test
    @DisplayName("PUT /api/source/book/{id} returns 404 when book not found")
    void updateBook_NotFound() throws Exception {
        Long bookId = 999L;
        Book book = new Book("Some Book", "Some Author");

        given(sourceService.updateBook(eq(bookId), any(Book.class))).willReturn(null);

        mockMvc.perform(put("/api/source/book/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("No book found with ID: " + bookId)));
    }

    @Test
    @DisplayName("PUT /api/source/book/{id} returns 400 for invalid ID")
    void updateBook_InvalidId() throws Exception {
        Book book = new Book("Some Book", "Some Author");

        mockMvc.perform(put("/api/source/book/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("PUT /api/source/book/{id} returns 400 when book is null")
    void updateBook_NullBook() throws Exception {
        mockMvc.perform(put("/api/source/book/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/source/video/{id} successfully updates a video")
    void updateVideo_Success() throws Exception {
        Long videoId = 1L;
        Video updatedVideo = new Video("Neural Networks Updated", "3Blue1Brown");
        updatedVideo.setId(videoId);
        updatedVideo.setPlatform("YouTube");

        given(sourceService.updateVideo(eq(videoId), any(Video.class))).willReturn(updatedVideo);

        mockMvc.perform(put("/api/source/video/" + videoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedVideo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(videoId))
                .andExpect(jsonPath("$.title").value("Neural Networks Updated"));
    }

    @Test
    @DisplayName("PUT /api/source/video/{id} returns 404 when video not found")
    void updateVideo_NotFound() throws Exception {
        Long videoId = 999L;
        Video video = new Video("Some Video", "Some Creator");

        given(sourceService.updateVideo(eq(videoId), any(Video.class))).willReturn(null);

        mockMvc.perform(put("/api/source/video/" + videoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("No video found with ID: " + videoId)));
    }

    @Test
    @DisplayName("PUT /api/source/video/{id} returns 400 for invalid ID")
    void updateVideo_InvalidId() throws Exception {
        Video video = new Video("Some Video", "Some Creator");

        mockMvc.perform(put("/api/source/video/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(video)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("PUT /api/source/video/{id} returns 400 when video is null")
    void updateVideo_NullVideo() throws Exception {
        mockMvc.perform(put("/api/source/video/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }

    // --- Update Article Tests ---
    @Test
    @DisplayName("PUT /api/source/article/{id} successfully updates an article")
    void updateArticle_Success() throws Exception {
        Long articleId = 1L;
        Article updatedArticle = new Article("Deep Learning Updated", "Yann LeCun");
        updatedArticle.setId(articleId);
        updatedArticle.setJournal("Nature");

        given(sourceService.updateArticle(eq(articleId), any(Article.class))).willReturn(updatedArticle);

        mockMvc.perform(put("/api/source/article/" + articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedArticle)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(articleId))
                .andExpect(jsonPath("$.title").value("Deep Learning Updated"));
    }

    @Test
    @DisplayName("PUT /api/source/article/{id} returns 404 when article not found")
    void updateArticle_NotFound() throws Exception {
        Long articleId = 999L;
        Article article = new Article("Some Article", "Some Author");

        given(sourceService.updateArticle(eq(articleId), any(Article.class))).willReturn(null);

        mockMvc.perform(put("/api/source/article/" + articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("No article found with ID: " + articleId)));
    }

    @Test
    @DisplayName("PUT /api/source/article/{id} returns 400 for invalid ID")
    void updateArticle_InvalidId() throws Exception {
        Article article = new Article("Some Article", "Some Author");

        mockMvc.perform(put("/api/source/article/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("PUT /api/source/article/{id} returns 400 when article is null")
    void updateArticle_NullArticle() throws Exception {
        mockMvc.perform(put("/api/source/article/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/source/sources creates a new submission and returns citation IDs")
    void createNewSubmissionReturnscitationIds() throws Exception {
        // Arrange - prepare request JSON
        BulkSourceRequest request = new BulkSourceRequest();
        UserDTO user = new UserDTO();
        user.setUsername("mattlabasan");
        request.setUser(user);

        SourceDTO s1 = new SourceDTO();
        s1.setMediaType("book");
        s1.setTitle("Deep Learning with Python");
        s1.setAuthor("Fran\u00e7ois Chollet");
        s1.setIsbn("9781617294433");
        SourceDTO s2 = new SourceDTO();
        s2.setMediaType("video");
        s2.setTitle("Understanding Neural Networks");
        s2.setAuthor("3Blue1Brown");
        s2.setPlatform("YouTube");

        request.setSources(Arrays.asList(s1, s2));

        SourceBatchResponse resp = new SourceBatchResponse(123L, Arrays.asList("10", "11"));
        given(sourceService.addOrAppendSources(any(BulkSourceRequest.class), any())).willReturn(resp);

        // Act & Assert
        mockMvc.perform(post("/api/source/sources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.submissionId").value(123))
                .andExpect(jsonPath("$.citationIds[0]").value("10"))
                .andExpect(jsonPath("$.citationIds[1]").value("11"));
    }

    @Test
    @DisplayName("POST /api/source/sources?submissionId=222 appends to existing submission and returns citation IDs")
    void appendToExistingSubmission() throws Exception {
        BulkSourceRequest request = new BulkSourceRequest();
        UserDTO user = new UserDTO();
        user.setUsername("alice");
        request.setUser(user);

        SourceDTO s = new SourceDTO();
        s.setMediaType("article");
        s.setTitle("Some Article");
        s.setAuthor("A Writer");
        request.setSources(Arrays.asList(s));

        SourceBatchResponse resp = new SourceBatchResponse(222L, Arrays.asList("55"));
        given(sourceService.addOrAppendSources(any(BulkSourceRequest.class), eq(222L))).willReturn(resp);

        mockMvc.perform(post("/api/source/sources?submissionId=222")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.submissionId").value(222))
                .andExpect(jsonPath("$.citationIds[0]").value("55"));
    }

    @Test
    @DisplayName("POST /api/source/sources returns 404 when submissionId not found")
    void returns404WhenSubmissionNotFound() throws Exception {
        BulkSourceRequest request = new BulkSourceRequest();
        request.setUser(new UserDTO());
        request.setSources(Arrays.asList(new SourceDTO()));

        // service throws ResourceNotFoundException which controller maps to 404
        given(sourceService.addOrAppendSources(any(BulkSourceRequest.class), eq(999L)))
                .willThrow(new ResourceNotFoundException("submissionId not found: 999"));

        mockMvc.perform(post("/api/source/sources?submissionId=999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/source/sources returns 400 when request has no sources")
    void addSources_EmptySourcesList() throws Exception {
        BulkSourceRequest request = new BulkSourceRequest();
        request.setUser(new UserDTO());
        request.setSources(new ArrayList<>());

        mockMvc.perform(post("/api/source/sources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("No sources provided in request"))
                .andExpect(jsonPath("$.citationIds").isEmpty());
    }

    @Test
    @DisplayName("POST /api/source/sources returns 400 when sources is null")
    void addSources_NullSourcesList() throws Exception {
        BulkSourceRequest request = new BulkSourceRequest();
        request.setUser(new UserDTO());
        request.setSources(null);

        mockMvc.perform(post("/api/source/sources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("No sources provided in request"));
    }

    @Test
    @DisplayName("POST /api/source/sources handles partial success with errors")
    void addSources_PartialSuccess() throws Exception {
        BulkSourceRequest request = new BulkSourceRequest();
        UserDTO user = new UserDTO();
        user.setUsername("testuser");
        request.setUser(user);

        SourceDTO s1 = new SourceDTO();
        s1.setMediaType("book");
        s1.setTitle("Valid Book");
        s1.setAuthor("Author Name");

        SourceDTO s2 = new SourceDTO();
        s2.setMediaType("invalid");
        s2.setTitle("Invalid Source");

        request.setSources(Arrays.asList(s1, s2));

        SourceBatchResponse resp = new SourceBatchResponse();
        resp.setSubmissionId(100L);
        resp.setCitationIds(Arrays.asList("1"));
        resp.setErrors(Arrays.asList("Source 2: Invalid media type 'invalid'"));

        given(sourceService.addOrAppendSources(any(BulkSourceRequest.class), any())).willReturn(resp);

        mockMvc.perform(post("/api/source/sources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.submissionId").value(100))
                .andExpect(jsonPath("$.citationIds[0]").value("1"))
                .andExpect(jsonPath("$.errors[0]").value("Source 2: Invalid media type 'invalid'"));
    }
}
