package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.model.Article;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CrossRefDoiServiceTest {

    private MockWebServer mockWebServer;
    private CrossRefDoiService crossRefDoiService;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String baseUrl = mockWebServer.url("/").toString();
        crossRefDoiService = new CrossRefDoiService(WebClient.builder(), baseUrl);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fetchArticleDataByDoiValidDoiReturnsArticle() {
        String doi = "10.1002/aaai.70040";
        String mockJsonResponse = """
                {
                  "status": "ok",
                  "message-type": "work",
                  "message": {
                    "DOI": "10.1002/aaai.70040",
                    "type": "journal-article",
                    "title": [
                      "Imitation learning is probably existentially safe"
                    ],
                    "author": [
                      {
                        "given": "Michael K.",
                        "family": "Cohen",
                        "sequence": "first"
                      },
                      {
                        "given": "Marcus",
                        "family": "Hutter",
                        "sequence": "additional"
                      }
                    ],
                    "container-title": [
                      "AI Magazine"
                    ],
                    "volume": "46",
                    "issue": "4",
                    "page": "282-295",
                    "published-online": {
                      "date-parts": [
                        [2025, 11, 21]
                      ]
                    },
                    "URL": "https://onlinelibrary.wiley.com/doi/10.1002/aaai.70040"
                  }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<Article> articleMono = crossRefDoiService.fetchArticleDataByDoi(doi);

        StepVerifier.create(articleMono)
                .assertNext(article -> {
                    assertNotNull(article);
                    assertEquals("Imitation learning is probably existentially safe", article.getTitle());
                    assertEquals("Michael K. Cohen, Marcus Hutter", article.getAuthor());
                    assertEquals("AI Magazine", article.getJournal());
                    assertEquals("46", article.getVolume());
                    assertEquals("4", article.getIssue());
                    assertEquals("282-295", article.getPages());
                    assertEquals(2025, article.getPublicationYear());
                    assertEquals(doi, article.getDoi());
                    assertEquals("https://onlinelibrary.wiley.com/doi/10.1002/aaai.70040",
                            article.getUrl());
                })
                .verifyComplete();
    }

    @Test
    void fetchArticleDataByDoiNoDoiReturnsEmptyMono() {
        Mono<Article> articleMono = crossRefDoiService.fetchArticleDataByDoi(null);
        StepVerifier.create(articleMono).verifyComplete();

        articleMono = crossRefDoiService.fetchArticleDataByDoi("");
        StepVerifier.create(articleMono).verifyComplete();
    }

    @Test
    void fetchArticleDataByDoiApiReturnsNoMessageReturnsEmptyMono() {
        String mockJsonResponse = """
                {
                  "status": "ok",
                  "message-type": "work"
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<Article> articleMono = crossRefDoiService.fetchArticleDataByDoi("10.1234/invalid");

        StepVerifier.create(articleMono).verifyComplete();
    }

    @Test
    void fetchArticleDataByDoiApiReturnsNoTitleReturnsEmptyMono() {
        String mockJsonResponse = """
                {
                  "status": "ok",
                  "message": {
                    "DOI": "10.1002/test",
                    "author": [
                      {
                        "given": "John",
                        "family": "Doe"
                      }
                    ]
                  }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<Article> articleMono = crossRefDoiService.fetchArticleDataByDoi("10.1002/test");

        StepVerifier.create(articleMono).verifyComplete();
    }

    @Test
    void fetchArticleDataByDoiApiReturnsPartialDataFillsAvailableFields() {
        String doi = "10.1234/partial";
        String mockJsonResponse = """
                {
                  "status": "ok",
                  "message": {
                    "title": [
                      "Partial Article Data"
                    ],
                    "author": [
                      {
                        "family": "Smith"
                      }
                    ],
                    "published-print": {
                      "date-parts": [
                        [2023, 5]
                      ]
                    }
                  }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<Article> articleMono = crossRefDoiService.fetchArticleDataByDoi(doi);

        StepVerifier.create(articleMono)
                .assertNext(article -> {
                    assertNotNull(article);
                    assertEquals("Partial Article Data", article.getTitle());
                    assertEquals("Smith", article.getAuthor());
                    assertNull(article.getJournal());
                    assertNull(article.getVolume());
                    assertNull(article.getIssue());
                    assertNull(article.getPages());
                    assertEquals(2023, article.getPublicationYear());
                    assertEquals(doi, article.getDoi());
                    assertEquals("https://doi.org/" + doi, article.getUrl());
                })
                .verifyComplete();
    }

    @Test
    void fetchArticleDataByDoiYearExtractedFromPublishedPrint() {
        String doi = "10.1234/print-date";
        String mockJsonResponse = """
                {
                  "status": "ok",
                  "message": {
                    "title": ["Article With Print Date"],
                    "published-print": {
                      "date-parts": [
                        [2022, 3, 15]
                      ]
                    }
                  }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<Article> articleMono = crossRefDoiService.fetchArticleDataByDoi(doi);

        StepVerifier.create(articleMono)
                .assertNext(article -> {
                    assertEquals(2022, article.getPublicationYear());
                })
                .verifyComplete();
    }

    @Test
    void fetchArticleDataByDoiYearExtractedFromIssued() {
        String doi = "10.1234/issued-date";
        String mockJsonResponse = """
                {
                  "status": "ok",
                  "message": {
                    "title": ["Article With Issued Date"],
                    "issued": {
                      "date-parts": [
                        [2021]
                      ]
                    }
                  }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<Article> articleMono = crossRefDoiService.fetchArticleDataByDoi(doi);

        StepVerifier.create(articleMono)
                .assertNext(article -> {
                    assertEquals(2021, article.getPublicationYear());
                })
                .verifyComplete();
    }

    @Test
    void fetchArticleDataByDoiMultipleAuthorsFormattedCorrectly() {
        String doi = "10.1234/multi-author";
        String mockJsonResponse = """
                {
                  "status": "ok",
                  "message": {
                    "title": ["Multi-Author Article"],
                    "author": [
                      {
                        "given": "Alice",
                        "family": "Johnson"
                      },
                      {
                        "given": "Bob",
                        "family": "Williams"
                      },
                      {
                        "given": "Charlie",
                        "family": "Brown"
                      }
                    ]
                  }
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<Article> articleMono = crossRefDoiService.fetchArticleDataByDoi(doi);

        StepVerifier.create(articleMono)
                .assertNext(article -> {
                    assertEquals("Alice Johnson, Bob Williams, Charlie Brown",
                            article.getAuthor());
                })
                .verifyComplete();
    }

    @Test
    void fetchArticleDataByDoi404ReturnsEmptyMono() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("Not Found"));

        Mono<Article> articleMono = crossRefDoiService.fetchArticleDataByDoi("10.1234/notfound");

        StepVerifier.create(articleMono).verifyComplete();
    }

    @Test
    void fetchArticleDataByDoiServerErrorReturnsEmptyMono() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        Mono<Article> articleMono = crossRefDoiService.fetchArticleDataByDoi("10.1234/error");

        StepVerifier.create(articleMono).verifyComplete();
    }
}

