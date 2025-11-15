package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GoogleBooksService {

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

        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("")
                                .queryParam("q", "isbn:" + isbn)
                                .queryParam("key", googleBooksApiKey)
                                .build())
                .retrieve()
                .bodyToMono(GoogleBooksApiResponse.class)
                .flatMap(apiResponse ->
                        parseGoogleBooksApiResponse(apiResponse, isbn));
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
         * List of items returned by the API.
         */
        private java.util.List<Item> items;

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
