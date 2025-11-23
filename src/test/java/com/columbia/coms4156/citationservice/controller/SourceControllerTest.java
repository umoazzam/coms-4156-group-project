package com.columbia.coms4156.citationservice.controller;

import com.columbia.coms4156.citationservice.controller.dto.BulkSourceRequest;
import com.columbia.coms4156.citationservice.controller.dto.SourceBatchResponse;
import com.columbia.coms4156.citationservice.controller.dto.SourceDTO;
import com.columbia.coms4156.citationservice.controller.dto.UserDTO;
import com.columbia.coms4156.citationservice.exception.ResourceNotFoundException;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("POST /api/source/sources creates a new submission and returns source ids")
    void createNewSubmissionReturnsSourceIds() throws Exception {
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
                .andExpect(jsonPath("$.sourceIds[0]").value("10"))
                .andExpect(jsonPath("$.sourceIds[1]").value("11"));
    }

    @Test
    @DisplayName("POST /api/source/sources?submissionId=222 appends to existing submission and returns source ids")
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
                .andExpect(jsonPath("$.sourceIds[0]").value("55"));
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
}

