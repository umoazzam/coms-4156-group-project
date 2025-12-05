package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GoogleBooksService {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleBooksService.class);

    /**
     * WebClient for making HTTP requests to the Google Books API.
     */
    private final WebClient webClient;
    /**
     * Google Books API key.
     */
    private final String googleBooksApiKey;

    /**
     * Constructs a new GoogleBooksService.
     * @param webClientBuilder WebClient builder for creating WebClient instances.
     * @param baseUrl The base URL for the Google Books API.
     * @param apiKey Google Books API key.
     */
    public GoogleBooksService(WebClient.Builder webClientBuilder,
            @Value("${google.books.api.base-url}") String baseUrl,
            @Value("${google.books.api.key}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.googleBooksApiKey = apiKey;
    }

    /**
     * Fetches book data from the Google Books API by ISBN.
     * @param isbn The ISBN of the book to fetch.
     * @return A Mono containing the Book, or an empty Mono if not found.
     */
    public Mono<Book> fetchBookDataByIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return Mono.empty();
        }

        LOGGER.info("Fetching book data from Google Books API for ISBN: {}", isbn);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/volumes")
                        .queryParam("q", "isbn:" + isbn)
                        .queryParam("key", googleBooksApiKey) // Preserving the API key
                        .build())
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        response -> {
                            LOGGER.warn("Google Books API returned 404 for ISBN: {}", isbn);
                            return Mono.empty();
                        })
                .bodyToMono(GoogleBooksApiResponse.class)
                .flatMap(apiResponse -> {
                    if (apiResponse.getTotalItems() == 0) {
                        LOGGER.info("Google Books API returned 0 items for ISBN: {}", isbn);
                        return Mono.empty();
                    }
                    LOGGER.info("Successfully fetched book data for ISBN: {}", isbn);
                    return parseGoogleBooksApiResponse(apiResponse, isbn);
                })
                .onErrorResume(e -> {
                    LOGGER.error("Error fetching data from Google Books API for ISBN: {}", isbn, e);
                    return Mono.empty();
                });
    }

    private Mono<Book> parseGoogleBooksApiResponse(
            GoogleBooksApiResponse apiResponse, String originalIsbn) {
        if (apiResponse == null || apiResponse.getItems() == null
                || apiResponse.getItems().isEmpty()) {
            return Mono.empty();
        }

        // Take the first item as the most relevant result
        GoogleBooksApiResponse.Item item = apiResponse.getItems().get(0);
        GoogleBooksApiResponse.VolumeInfo volumeInfo = item.getVolumeInfo();

        if (volumeInfo == null || volumeInfo.getTitle() == null
                || volumeInfo.getTitle().isBlank()) {
            return Mono.empty();
        }

        Book book = new Book();
        book.setTitle(volumeInfo.getTitle());
        book.setPublisher(volumeInfo.getPublisher());
        book.setPublicationYear(parsePublicationYear(volumeInfo.getPublishedDate()));
        book.setIsbn(originalIsbn); // Ensure ISBN is set from the request

        // Authors
        if (volumeInfo.getAuthors() != null && !volumeInfo.getAuthors().isEmpty()) {
            book.setAuthor(String.join(", ", volumeInfo.getAuthors()));
        }

        return Mono.just(book);
    }

    /**
     * The expected length of a year string.
     */
    private static final int YEAR_LENGTH = 4;

    Integer parsePublicationYear(String publishedDate) {
        if (publishedDate == null || publishedDate.isEmpty()) {
            return null;
        }
        // The publishedDate can be "YYYY-MM-DD", "YYYY-MM", or "YYYY"
        try {
            return Integer.parseInt(publishedDate.substring(0, YEAR_LENGTH));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Inner classes to map the Google Books API response structure
    private static class GoogleBooksApiResponse {
        /**
         * Total number of items found.
         */
        private int totalItems;

        /**
         * List of items returned by the API.
         */
        private java.util.List<Item> items;

        /**
         * Gets the total number of items.
         *
         * @return The total number of items.
         */
        public int getTotalItems() {
            return totalItems;
        }

        /**
         * Sets the total number of items.
         *
         * @param pTotalItems The total number of items.
         */
        public void setTotalItems(int pTotalItems) {
            this.totalItems = pTotalItems;
        }

        /**
         * Gets the list of items.
         * @return The list of items.
         */
        public java.util.List<Item> getItems() {
            return items;
        }

        /**
         * Sets the list of items.
         * @param pItems The list of items.
         */
        public void setItems(java.util.List<Item> pItems) {
            this.items = pItems;
        }

        private static class Item {
            /**
             * Volume information for the item.
             */
            private VolumeInfo volumeInfo;

            /**
             * Gets the volume information.
             * @return The volume information.
             */
            public VolumeInfo getVolumeInfo() {
                return volumeInfo;
            }

            /**
             * Sets the volume information.
             * @param pVolumeInfo The volume information.
             */
            public void setVolumeInfo(VolumeInfo pVolumeInfo) {
                this.volumeInfo = pVolumeInfo;
            }
        }

        private static class VolumeInfo {
            /**
             * The title of the volume.
             */
            private String title;
            /**
             * The authors of the volume.
             */
            private java.util.List<String> authors;
            /**
             * The publisher of the volume.
             */
            private String publisher;
            /**
             * The published date of the volume.
             */
            private String publishedDate;

            /**
             * Gets the title.
             * @return The title.
             */
            public String getTitle() {
                return title;
            }

            /**
             * Sets the title.
             * @param pTitle The title.
             */
            public void setTitle(String pTitle) {
                this.title = pTitle;
            }

            /**
             * Gets the authors.
             * @return The authors.
             */
            public java.util.List<String> getAuthors() {
                return authors;
            }

            /**
             * Sets the authors.
             * @param pAuthors The authors.
             */
            public void setAuthors(java.util.List<String> pAuthors) {
                this.authors = pAuthors;
            }

            /**
             * Gets the publisher.
             * @return The publisher.
             */
            public String getPublisher() {
                return publisher;
            }

            /**
             * Sets the publisher.
             * @param pPublisher The publisher.
             */
            public void setPublisher(String pPublisher) {
                this.publisher = pPublisher;
            }

            /**
             * Gets the published date.
             * @return The published date.
             */
            public String getPublishedDate() {
                return publishedDate;
            }

            /**
             * Sets the published date.
             * @param pPublishedDate The published date.
             */
            public void setPublishedDate(String pPublishedDate) {
                this.publishedDate = pPublishedDate;
            }
        }
    }
}
