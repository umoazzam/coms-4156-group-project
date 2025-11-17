package com.columbia.coms4156.citationservice.controller;

import com.columbia.coms4156.citationservice.controller.dto.BulkSourceRequest;
import com.columbia.coms4156.citationservice.controller.dto.SourceDTO;
import com.columbia.coms4156.citationservice.controller.dto.UserDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CitationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/source/sources then GET /api/cite/group/{submissionId} returns citations")
    void generateCitationsForGroup_IntegrationTest() throws Exception {
        // Arrange: Create a bulk source request
        UserDTO user = new UserDTO();
        user.setUsername("integration_test_user");

        SourceDTO bookSource = new SourceDTO();
        bookSource.setMediaType("book");
        bookSource.setTitle("The Lord of the Rings");
        bookSource.setAuthor("J.R.R. Tolkien");
        bookSource.setYear(1954);
        bookSource.setPublisher("Allen & Unwin");

        SourceDTO videoSource = new SourceDTO();
        videoSource.setMediaType("video");
        videoSource.setTitle("The Shawshank Redemption");
        videoSource.setAuthor("Frank Darabont");
        videoSource.setPlatform("Castle Rock Entertainment");
        videoSource.setYear(1994);

        BulkSourceRequest bulkRequest = new BulkSourceRequest();
        bulkRequest.setUser(user);
        bulkRequest.setSources(Arrays.asList(bookSource, videoSource));

        // Act 1: POST to create the submission and sources
        MvcResult postResult = mockMvc.perform(post("/api/source/sources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bulkRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.submissionId").exists())
                .andExpect(jsonPath("$.sourceIds").isArray())
                .andExpect(jsonPath("$.sourceIds.length()").value(2))
                .andReturn();

        String responseBody = postResult.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        long submissionId = responseJson.get("submissionId").asLong();

        // Act 2: GET the group citations
        mockMvc.perform(get("/api/cite/group/{submissionId}", submissionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.submissionId").value(submissionId))
                .andExpect(jsonPath("$.Citations").isMap())
                .andExpect(jsonPath("$.Citations.length()").value(2))
                .andExpect(jsonPath("$.Citations.*").isNotEmpty());
    }
}
