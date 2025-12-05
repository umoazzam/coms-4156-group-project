package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.model.Article;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service for fetching article data from the CrossRef API using DOI.
 */
@Service
public class CrossRefDoiService {

    /**
     * WebClient for making HTTP requests to the CrossRef API.
     */
    private final WebClient webClient;

    /**
     * HTTP status code for NOT FOUND.
     */
    private static final int HTTP_NOT_FOUND = 404;

    /**
     * Constructs a new CrossRefDoiService.
     * @param webClientBuilder WebClient builder for creating WebClient instances.
     * @param baseUrl The base URL for the CrossRef API.
     */
    public CrossRefDoiService(WebClient.Builder webClientBuilder,
                              @Value("${crossref.api.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    /**
     * Fetches article data from the CrossRef API by DOI.
     * @param doi The DOI of the article to fetch.
     * @return A Mono containing the Article, or an empty Mono if not found.
     */
    public Mono<Article> fetchArticleDataByDoi(String doi) {
        if (doi == null || doi.isEmpty()) {
            return Mono.empty();
        }

        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/works/" + doi)
                                .build())
                .retrieve()
                .onStatus(status -> status.value() == HTTP_NOT_FOUND,
                        response -> Mono.empty())
                .bodyToMono(CrossRefApiResponse.class)
                .flatMap(apiResponse ->
                        parseCrossRefApiResponse(apiResponse, doi))
                .onErrorResume(e -> Mono.empty());
    }

    /**
     * Parses the CrossRef API response and converts it to an Article object.
     *
     * @param apiResponse the API response to parse
     * @param originalDoi the original DOI used in the request
     * @return A Mono containing the parsed Article, or empty if parsing fails
     */
    private Mono<Article> parseCrossRefApiResponse(
            CrossRefApiResponse apiResponse, String originalDoi) {
        if (apiResponse == null || apiResponse.getMessage() == null) {
            return Mono.empty();
        }

        CrossRefApiResponse.Message message = apiResponse.getMessage();

        // Title is required
        if (message.getTitle() == null || message.getTitle().isEmpty()) {
            return Mono.empty();
        }

        Article article = new Article();

        // Set title (from array, take first element)
        article.setTitle(message.getTitle().get(0));

        // Set author (combine all authors)
        if (message.getAuthor() != null && !message.getAuthor().isEmpty()) {
            StringBuilder authors = new StringBuilder();
            for (int i = 0; i < message.getAuthor().size(); i++) {
                CrossRefApiResponse.Author author = message.getAuthor().get(i);
                if (author.getGiven() != null && author.getFamily() != null) {
                    authors.append(author.getGiven()).append(" ").append(author.getFamily());
                } else if (author.getFamily() != null) {
                    authors.append(author.getFamily());
                }
                if (i < message.getAuthor().size() - 1) {
                    authors.append(", ");
                }
            }
            article.setAuthor(authors.toString());
        }

        // Set DOI
        article.setDoi(originalDoi);

        // Set journal (from container-title array)
        if (message.getContainerTitle() != null && !message.getContainerTitle().isEmpty()) {
            article.setJournal(message.getContainerTitle().get(0));
        }

        // Set volume
        article.setVolume(message.getVolume());

        // Set issue
        article.setIssue(message.getIssue());

        // Set pages
        article.setPages(message.getPage());

        // Set publication year (from published-online or published-print)
        Integer year = extractYear(message);
        article.setPublicationYear(year);

        // Set URL (from response or generate from DOI)
        if (message.getUrl() != null && !message.getUrl().isEmpty()) {
            article.setUrl(message.getUrl());
        } else if (originalDoi != null && !originalDoi.isEmpty()) {
            article.setUrl("https://doi.org/" + originalDoi);
        }

        return Mono.just(article);
    }

    /**
     * Extracts the publication year from the message.
     * Tries published-online first, then published-print, then issued.
     *
     * @param message the message containing date information
     * @return the publication year, or null if not found
     */
    private Integer extractYear(CrossRefApiResponse.Message message) {
        // Try published-online first
        if (message.getPublishedOnline() != null
                && message.getPublishedOnline().getDateParts() != null
                && !message.getPublishedOnline().getDateParts().isEmpty()
                && !message.getPublishedOnline().getDateParts().get(0).isEmpty()) {
            return message.getPublishedOnline().getDateParts().get(0).get(0);
        }

        // Try published-print
        if (message.getPublishedPrint() != null
                && message.getPublishedPrint().getDateParts() != null
                && !message.getPublishedPrint().getDateParts().isEmpty()
                && !message.getPublishedPrint().getDateParts().get(0).isEmpty()) {
            return message.getPublishedPrint().getDateParts().get(0).get(0);
        }

        // Try issued
        if (message.getIssued() != null
                && message.getIssued().getDateParts() != null
                && !message.getIssued().getDateParts().isEmpty()
                && !message.getIssued().getDateParts().get(0).isEmpty()) {
            return message.getIssued().getDateParts().get(0).get(0);
        }

        return null;
    }

    // Inner classes to map the CrossRef API response structure
    private static class CrossRefApiResponse {
        /**
         * Status of the API response.
         */
        private String status;
        /**
         * Message containing the article data.
         */
        private Message message;

        /**
         * Gets the status.
         * @return the status
         */
        public String getStatus() {
            return status;
        }

        /**
         * Sets the status.
         * @param statusParam the status to set
         */
        public void setStatus(String statusParam) {
            this.status = statusParam;
        }

        /**
         * Gets the message.
         * @return the message
         */
        public Message getMessage() {
            return message;
        }

        /**
         * Sets the message.
         * @param messageParam the message to set
         */
        public void setMessage(Message messageParam) {
            this.message = messageParam;
        }

        private static class Message {
            /**
             * Title of the article.
             */
            private List<String> title;
            /**
             * Authors of the article.
             */
            private List<Author> author;
            /**
             * Volume number.
             */
            private String volume;
            /**
             * Issue number.
             */
            private String issue;
            /**
             * Page numbers.
             */
            private String page;
            /**
             * Container title (journal name).
             */
            @JsonProperty("container-title")
            private List<String> containerTitle;
            /**
             * Published online date information.
             */
            @JsonProperty("published-online")
            private DateInfo publishedOnline;
            /**
             * Published print date information.
             */
            @JsonProperty("published-print")
            private DateInfo publishedPrint;
            /**
             * Issued date information.
             */
            private DateInfo issued;
            /**
             * URL of the article.
             */
            @JsonProperty("URL")
            private String url;

            // Getters and Setters
            /**
             * Gets the title.
             * @return the title
             */
            public List<String> getTitle() {
                return title;
            }

            /**
             * Sets the title.
             * @param titleParam the title to set
             */
            public void setTitle(List<String> titleParam) {
                this.title = titleParam;
            }

            /**
             * Gets the author list.
             * @return the author list
             */
            public List<Author> getAuthor() {
                return author;
            }

            /**
             * Sets the author list.
             * @param authorParam the author list to set
             */
            public void setAuthor(List<Author> authorParam) {
                this.author = authorParam;
            }

            /**
             * Gets the volume.
             * @return the volume
             */
            public String getVolume() {
                return volume;
            }

            /**
             * Sets the volume.
             * @param volumeParam the volume to set
             */
            public void setVolume(String volumeParam) {
                this.volume = volumeParam;
            }

            /**
             * Gets the issue.
             * @return the issue
             */
            public String getIssue() {
                return issue;
            }

            /**
             * Sets the issue.
             * @param issueParam the issue to set
             */
            public void setIssue(String issueParam) {
                this.issue = issueParam;
            }

            /**
             * Gets the page numbers.
             * @return the page numbers
             */
            public String getPage() {
                return page;
            }

            /**
             * Sets the page numbers.
             * @param pageParam the page numbers to set
             */
            public void setPage(String pageParam) {
                this.page = pageParam;
            }

            /**
             * Gets the container title.
             * @return the container title
             */
            public List<String> getContainerTitle() {
                return containerTitle;
            }

            /**
             * Sets the container title.
             * @param containerTitleParam the container title to set
             */
            public void setContainerTitle(List<String> containerTitleParam) {
                this.containerTitle = containerTitleParam;
            }

            /**
             * Gets the published online date info.
             * @return the published online date info
             */
            public DateInfo getPublishedOnline() {
                return publishedOnline;
            }

            /**
             * Sets the published online date info.
             * @param publishedOnlineParam the published online date info to set
             */
            public void setPublishedOnline(DateInfo publishedOnlineParam) {
                this.publishedOnline = publishedOnlineParam;
            }

            /**
             * Gets the published print date info.
             * @return the published print date info
             */
            public DateInfo getPublishedPrint() {
                return publishedPrint;
            }

            /**
             * Sets the published print date info.
             * @param publishedPrintParam the published print date info to set
             */
            public void setPublishedPrint(DateInfo publishedPrintParam) {
                this.publishedPrint = publishedPrintParam;
            }

            /**
             * Gets the issued date info.
             * @return the issued date info
             */
            public DateInfo getIssued() {
                return issued;
            }

            /**
             * Sets the issued date info.
             * @param issuedParam the issued date info to set
             */
            public void setIssued(DateInfo issuedParam) {
                this.issued = issuedParam;
            }

            /**
             * Gets the URL.
             * @return the URL
             */
            public String getUrl() {
                return url;
            }

            /**
             * Sets the URL.
             * @param urlParam the URL to set
             */
            public void setUrl(String urlParam) {
                this.url = urlParam;
            }
        }

        private static class Author {
            /**
             * Given name of the author.
             */
            private String given;
            /**
             * Family name of the author.
             */
            private String family;
            /**
             * Sequence of the author.
             */
            private String sequence;

            /**
             * Gets the given name.
             * @return the given name
             */
            public String getGiven() {
                return given;
            }

            /**
             * Sets the given name.
             * @param givenParam the given name to set
             */
            public void setGiven(String givenParam) {
                this.given = givenParam;
            }

            /**
             * Gets the family name.
             * @return the family name
             */
            public String getFamily() {
                return family;
            }

            /**
             * Sets the family name.
             * @param familyParam the family name to set
             */
            public void setFamily(String familyParam) {
                this.family = familyParam;
            }

            /**
             * Gets the sequence.
             * @return the sequence
             */
            public String getSequence() {
                return sequence;
            }

            /**
             * Sets the sequence.
             * @param sequenceParam the sequence to set
             */
            public void setSequence(String sequenceParam) {
                this.sequence = sequenceParam;
            }
        }

        private static class DateInfo {
            /**
             * Date parts array containing year, month, day.
             */
            @JsonProperty("date-parts")
            private List<List<Integer>> dateParts;

            /**
             * Gets the date parts.
             * @return the date parts
             */
            public List<List<Integer>> getDateParts() {
                return dateParts;
            }

            /**
             * Sets the date parts.
             * @param datePartsParam the date parts to set
             */
            public void setDateParts(List<List<Integer>> datePartsParam) {
                this.dateParts = datePartsParam;
            }
        }
    }
}
