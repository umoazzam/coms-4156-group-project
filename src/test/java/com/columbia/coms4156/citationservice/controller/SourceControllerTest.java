package com.columbia.coms4156.citationservice.controller;

import com.columbia.coms4156.citationservice.controller.dto.BulkSourceRequest;
import com.columbia.coms4156.citationservice.controller.dto.SourceBatchResponse;
import com.columbia.coms4156.citationservice.controller.dto.SourceDTO;
import com.columbia.coms4156.citationservice.controller.dto.UserDTO;
import com.columbia.coms4156.citationservice.exception.ResourceNotFoundException;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.model.Article;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        // --- Book Tests ---

        @Test
        @DisplayName("POST /api/source/book creates a new book")
        void createBook_Success() throws Exception {
                Book book = new Book();
                book.setTitle("Test Book");
                book.setAuthor("Test Author");
                book.setPublisher("Test Publisher");
                book.setPublicationYear(2023);

                Book savedBook = new Book();
                savedBook.setId(1L);
                savedBook.setTitle("Test Book");
                savedBook.setAuthor("Test Author");
                savedBook.setPublisher("Test Publisher");
                savedBook.setPublicationYear(2023);

                given(sourceService.saveBook(any(Book.class))).willReturn(savedBook);

                mockMvc.perform(post("/api/source/book")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(book)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("Test Book"));
        }

        @Test
        @DisplayName("GET /api/source/book returns all books")
        void getAllBooks_Success() throws Exception {
                Book b1 = new Book();
                b1.setId(1L);
                b1.setTitle("Book One");
                Book b2 = new Book();
                b2.setId(2L);
                b2.setTitle("Book Two");

                given(sourceService.getAllBooks()).willReturn(Arrays.asList(b1, b2));

                mockMvc.perform(get("/api/source/book"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].title").value("Book One"))
                                .andExpect(jsonPath("$[1].title").value("Book Two"));
        }

        @Test
        @DisplayName("GET /api/source/book/{id} returns book when found")
        void getBookById_Success() throws Exception {
                Book book = new Book();
                book.setId(1L);
                book.setTitle("Book One");

                given(sourceService.findBookById(1L)).willReturn(Optional.of(book));

                mockMvc.perform(get("/api/source/book/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("Book One"));
        }

        @Test
        @DisplayName("GET /api/source/book/{id} returns 404 when not found")
        void getBookById_NotFound() throws Exception {
                given(sourceService.findBookById(1L)).willReturn(Optional.empty());

                mockMvc.perform(get("/api/source/book/1"))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("PUT /api/source/book/{id} updates book when found")
        void updateBook_Success() throws Exception {
                Book book = new Book();
                book.setTitle("Updated Book");
                book.setAuthor("Updated Author");
                book.setPublisher("Updated Publisher");
                book.setPublicationYear(2023);

                Book updatedBook = new Book();
                updatedBook.setId(1L);
                updatedBook.setTitle("Updated Book");
                updatedBook.setAuthor("Updated Author");
                updatedBook.setPublisher("Updated Publisher");
                updatedBook.setPublicationYear(2023);

                given(sourceService.updateBook(eq(1L), any(Book.class))).willReturn(updatedBook);

                mockMvc.perform(put("/api/source/book/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(book)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Updated Book"));
        }

        @Test
        @DisplayName("PUT /api/source/book/{id} returns 404 when book not found")
        void updateBook_NotFound() throws Exception {
                Book book = new Book();
                book.setId(1L);
                book.setTitle("Updated Book");
                book.setAuthor("Updated Author");
                book.setPublisher("Updated Publisher");
                book.setPublicationYear(2023);

                given(sourceService.updateBook(eq(1L), any(Book.class))).willReturn(null);

                mockMvc.perform(put("/api/source/book/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(book)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("DELETE /api/source/book/{id} deletes book when found")
        void deleteBook_Success() throws Exception {
                Book book = new Book();
                book.setId(1L);
                given(sourceService.findBookById(1L)).willReturn(Optional.of(book));

                mockMvc.perform(delete("/api/source/book/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("DELETE /api/source/book/{id} returns 404 when book not found")
        void deleteBook_NotFound() throws Exception {
                given(sourceService.findBookById(1L)).willReturn(Optional.empty());

                mockMvc.perform(delete("/api/source/book/1"))
                                .andExpect(status().isNotFound());
        }

        // --- Video Tests ---

        @Test
        @DisplayName("POST /api/source/video creates a new video")
        void createVideo_Success() throws Exception {
                Video video = new Video();
                video.setTitle("Test Video");
                video.setAuthor("Test Author");
                video.setReleaseYear(2023);

                Video savedVideo = new Video();
                savedVideo.setId(1L);
                savedVideo.setTitle("Test Video");
                savedVideo.setAuthor("Test Author");
                savedVideo.setReleaseYear(2023);

                given(sourceService.saveVideo(any(Video.class))).willReturn(savedVideo);

                mockMvc.perform(post("/api/source/video")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(video)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("Test Video"));
        }

        @Test
        @DisplayName("GET /api/source/video returns all videos")
        void getAllVideos_Success() throws Exception {
                Video v1 = new Video();
                v1.setId(1L);
                v1.setTitle("Video One");
                Video v2 = new Video();
                v2.setId(2L);
                v2.setTitle("Video Two");

                given(sourceService.getAllVideos()).willReturn(Arrays.asList(v1, v2));

                mockMvc.perform(get("/api/source/video"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].title").value("Video One"))
                                .andExpect(jsonPath("$[1].title").value("Video Two"));
        }

        @Test
        @DisplayName("GET /api/source/video/{id} returns video when found")
        void getVideoById_Success() throws Exception {
                Video video = new Video();
                video.setId(1L);
                video.setTitle("Video One");

                given(sourceService.findVideoById(1L)).willReturn(Optional.of(video));

                mockMvc.perform(get("/api/source/video/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("Video One"));
        }

        @Test
        @DisplayName("GET /api/source/video/{id} returns 404 when not found")
        void getVideoById_NotFound() throws Exception {
                given(sourceService.findVideoById(1L)).willReturn(Optional.empty());

                mockMvc.perform(get("/api/source/video/1"))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("PUT /api/source/video/{id} updates video when found")
        void updateVideo_Success() throws Exception {
                Video video = new Video();
                video.setTitle("Updated Video");
                video.setAuthor("Updated Author");
                video.setReleaseYear(2023);

                Video updatedVideo = new Video();
                updatedVideo.setId(1L);
                updatedVideo.setTitle("Updated Video");
                updatedVideo.setAuthor("Updated Author");
                updatedVideo.setReleaseYear(2023);

                given(sourceService.updateVideo(eq(1L), any(Video.class))).willReturn(updatedVideo);

                mockMvc.perform(put("/api/source/video/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(video)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Updated Video"));
        }

        @Test
        @DisplayName("PUT /api/source/video/{id} returns 404 when video not found")
        void updateVideo_NotFound() throws Exception {
                Video video = new Video();
                video.setTitle("Updated Video");
                video.setAuthor("Updated Author");
                video.setReleaseYear(2023);

                given(sourceService.updateVideo(eq(1L), any(Video.class))).willReturn(null);

                mockMvc.perform(put("/api/source/video/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(video)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("DELETE /api/source/video/{id} deletes video when found")
        void deleteVideo_Success() throws Exception {
                Video video = new Video();
                video.setId(1L);
                given(sourceService.findVideoById(1L)).willReturn(Optional.of(video));

                mockMvc.perform(delete("/api/source/video/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("DELETE /api/source/video/{id} returns 404 when video not found")
        void deleteVideo_NotFound() throws Exception {
                given(sourceService.findVideoById(1L)).willReturn(Optional.empty());

                mockMvc.perform(delete("/api/source/video/1"))
                                .andExpect(status().isNotFound());
        }

        // --- Article Tests ---

        @Test
        @DisplayName("POST /api/source/article creates a new article")
        void createArticle_Success() throws Exception {
                Article article = new Article();
                article.setTitle("Test Article");
                article.setAuthor("Test Author");
                article.setJournal("Test Journal");
                article.setPublicationYear(2023);

                Article savedArticle = new Article();
                savedArticle.setId(1L);
                savedArticle.setTitle("Test Article");
                savedArticle.setAuthor("Test Author");
                savedArticle.setJournal("Test Journal");
                savedArticle.setPublicationYear(2023);

                given(sourceService.saveArticle(any(Article.class))).willReturn(savedArticle);

                mockMvc.perform(post("/api/source/article")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(article)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("Test Article"));
        }

        @Test
        @DisplayName("GET /api/source/article returns all articles")
        void getAllArticles_Success() throws Exception {
                Article a1 = new Article();
                a1.setId(1L);
                a1.setTitle("Article One");
                Article a2 = new Article();
                a2.setId(2L);
                a2.setTitle("Article Two");

                given(sourceService.getAllArticles()).willReturn(Arrays.asList(a1, a2));

                mockMvc.perform(get("/api/source/article"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].title").value("Article One"))
                                .andExpect(jsonPath("$[1].title").value("Article Two"));
        }

        @Test
        @DisplayName("GET /api/source/article/{id} returns article when found")
        void getArticleById_Success() throws Exception {
                Article article = new Article();
                article.setId(1L);
                article.setTitle("Article One");

                given(sourceService.findArticleById(1L)).willReturn(Optional.of(article));

                mockMvc.perform(get("/api/source/article/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("Article One"));
        }

        @Test
        @DisplayName("GET /api/source/article/{id} returns 404 when not found")
        void getArticleById_NotFound() throws Exception {
                given(sourceService.findArticleById(1L)).willReturn(Optional.empty());

                mockMvc.perform(get("/api/source/article/1"))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("PUT /api/source/article/{id} updates article when found")
        void updateArticle_Success() throws Exception {
                Article article = new Article();
                article.setTitle("Updated Article");
                article.setAuthor("Updated Author");
                article.setJournal("Updated Journal");
                article.setPublicationYear(2023);

                Article updatedArticle = new Article();
                updatedArticle.setId(1L);
                updatedArticle.setTitle("Updated Article");
                updatedArticle.setAuthor("Updated Author");
                updatedArticle.setJournal("Updated Journal");
                updatedArticle.setPublicationYear(2023);

                given(sourceService.updateArticle(eq(1L), any(Article.class))).willReturn(updatedArticle);

                mockMvc.perform(put("/api/source/article/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(article)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("Updated Article"));
        }

        @Test
        @DisplayName("PUT /api/source/article/{id} returns 404 when article not found")
        void updateArticle_NotFound() throws Exception {
                Article article = new Article();
                article.setTitle("Updated Article");
                article.setAuthor("Updated Author");
                article.setJournal("Updated Journal");
                article.setPublicationYear(2023);

                given(sourceService.updateArticle(eq(1L), any(Article.class))).willReturn(null);

                mockMvc.perform(put("/api/source/article/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(article)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("DELETE /api/source/article/{id} deletes article when found")
        void deleteArticle_Success() throws Exception {
                Article article = new Article();
                article.setId(1L);
                given(sourceService.findArticleById(1L)).willReturn(Optional.of(article));

                mockMvc.perform(delete("/api/source/article/1"))
                                .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("DELETE /api/source/article/{id} returns 404 when article not found")
        void deleteArticle_NotFound() throws Exception {
                given(sourceService.findArticleById(1L)).willReturn(Optional.empty());

                mockMvc.perform(delete("/api/source/article/1"))
                                .andExpect(status().isNotFound());
        }
}
