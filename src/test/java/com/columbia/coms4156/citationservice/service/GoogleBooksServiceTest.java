package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.model.Book;
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

class GoogleBooksServiceTest {

    private MockWebServer mockWebServer;
    private GoogleBooksService googleBooksService;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        String baseUrl = mockWebServer.url("/").toString();
        googleBooksService = new GoogleBooksService(WebClient.builder(), baseUrl, "test-api-key");
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void fetchBookDataByIsbnValidIsbnReturnsBook() {
        String isbn = "9780140449112";
        String mockJsonResponse = """
                {
                  "kind": "books#volumes",
                  "totalItems": 1,
                  "items": [
                    {
                      "kind": "books#volume",
                      "id": "s1gV_Sw4_xwC",
                      "etag": "y1gV_Sw4_xwC",
                      "volumeInfo": {
                        "title": "The Lord of the Rings", 
                        "authors": [
                          "J.R.R. Tolkien"
                        ],
                        "publisher": "Houghton Mifflin Harcourt",
                        "publishedDate": "2012-02-15",
                        "industryIdentifiers": [
                          {
                            "type": "ISBN_13",
                            "identifier": "9780140449112"
                          },
                          {
                            "type": "ISBN_10",
                            "identifier": "0140449116"
                          }
                        ]
                      }
                    }
                  ]
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<Book> bookMono = googleBooksService.fetchBookDataByIsbn(isbn);

        StepVerifier.create(bookMono)
                .assertNext(book -> {
                    assertNotNull(book);
                    assertEquals("The Lord of the Rings", book.getTitle());
                    assertEquals("J.R.R. Tolkien", book.getAuthor());
                    assertEquals("Houghton Mifflin Harcourt", book.getPublisher());
                    assertEquals(2012, book.getPublicationYear());
                    assertEquals(isbn, book.getIsbn()); // Should be the original ISBN passed
                })
                .verifyComplete();
    }

    @Test
    void fetchBookDataByIsbnNoIsbnReturnsEmptyMono() {
        Mono<Book> bookMono = googleBooksService.fetchBookDataByIsbn(null);
        StepVerifier.create(bookMono).verifyComplete();

        bookMono = googleBooksService.fetchBookDataByIsbn("");
        StepVerifier.create(bookMono).verifyComplete();
    }

    @Test
    void fetchBookDataByIsbnApiReturnsNoItemsReturnsEmptyMono() {
        String mockJsonResponse = """
                {
                  "kind": "books#volumes",
                  "totalItems": 0
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<Book> bookMono = googleBooksService.fetchBookDataByIsbn("invalid-isbn");

        StepVerifier.create(bookMono).verifyComplete();
    }

    @Test
    void fetchBookDataByIsbnApiReturnsEmptyVolumeInfoReturnsEmptyMono() {
        String mockJsonResponse = """
                {
                  "kind": "books#volumes",
                  "totalItems": 1,
                  "items": [
                    {
                      "kind": "books#volume",
                      "id": "s1gV_Sw4_xwC",
                      "etag": "y1gV_Sw4_xwC",
                      "volumeInfo": {}
                    }
                  ]
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<Book> bookMono = googleBooksService.fetchBookDataByIsbn("9780140449112");

        StepVerifier.create(bookMono)
                .verifyComplete();
    }

    @Test
    void fetchBookDataByIsbnApiReturnsPartialDataFillsAvailableFields() {
        String isbn = "9780140449112";
        String mockJsonResponse = """
                {
                  "kind": "books#volumes",
                  "totalItems": 1,
                  "items": [
                    {
                      "volumeInfo": {
                        "title": "Partial Book",
                        "publishedDate": "2020"
                      }
                    }
                  ]
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockJsonResponse)
                .addHeader("Content-Type", "application/json"));

        Mono<Book> bookMono = googleBooksService.fetchBookDataByIsbn(isbn);

        StepVerifier.create(bookMono)
                .assertNext(book -> {
                    assertNotNull(book);
                    assertEquals("Partial Book", book.getTitle());
                    assertNull(book.getAuthor());
                    assertNull(book.getPublisher());
                    assertEquals(2020, book.getPublicationYear());
                    assertEquals(isbn, book.getIsbn());
                })
                .verifyComplete();
    }

    @Test
    void parsePublicationYearVariousFormats() {
        assertEquals(2020, googleBooksService.parsePublicationYear("2020-01-01"));
        assertEquals(2020, googleBooksService.parsePublicationYear("2020-01"));
        assertEquals(2020, googleBooksService.parsePublicationYear("2020"));
        assertNull(googleBooksService.parsePublicationYear("invalid-date"));
        assertNull(googleBooksService.parsePublicationYear(null));
        assertNull(googleBooksService.parsePublicationYear(""));
    }
}
