package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.repository.BookRepository;
import com.columbia.coms4156.citationservice.repository.VideoRepository;
import com.columbia.coms4156.citationservice.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for citation-related business logic and citation generation.
 * Handles CRUD operations for various citation types and citation formatting.
 * Currently supports books, videos, and articles.
 */
@Service
public class CitationService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private ArticleRepository articleRepository;

    // CITATION GENERATION METHODS
    public String generateMLACitation(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        StringBuilder citation = new StringBuilder();

        // Simple Implementation: Assumes first, last
        if (book.getAuthor() != null && !book.getAuthor().trim().isEmpty()) {
            citation.append(formatAuthorName(book.getAuthor())).append(". ");
        }

        if (book.getTitle() != null && !book.getTitle().trim().isEmpty()) {
            citation.append("_").append(book.getTitle()).append("_. ");
        }

        if (book.getPublisher() != null && !book.getPublisher().trim().isEmpty()) {
            citation.append(book.getPublisher());
            if (book.getPublicationYear() != null) {
                citation.append(", ");
            } else {
                citation.append(". ");
            }
        }

        if (book.getPublicationYear() != null) {
            citation.append(book.getPublicationYear()).append(".");
        }

        return citation.toString().trim();
    }

    public String generateMLACitation(Video video) {
        if (video == null) {
            throw new IllegalArgumentException("Video cannot be null");
        }
        StringBuilder citation = new StringBuilder();
        if (video.getAuthor() != null && !video.getAuthor().trim().isEmpty()) {
            citation.append(formatAuthorName(video.getAuthor())).append(". ");
        }
        if (video.getTitle() != null && !video.getTitle().trim().isEmpty()) {
            citation.append("_").append(video.getTitle()).append("_. ");
        }
        if (video.getPlatform() != null && !video.getPlatform().trim().isEmpty()) {
            citation.append(video.getPlatform());
            if (video.getReleaseYear() != null) citation.append(", "); else citation.append(". ");
        }
        if (video.getReleaseYear() != null) {
            citation.append(video.getReleaseYear()).append(".");
        }
        return citation.toString().trim();
    }

    public String generateMLACitation(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("Article cannot be null");
        }
        StringBuilder citation = new StringBuilder();
        if (article.getAuthor() != null && !article.getAuthor().trim().isEmpty()) {
            citation.append(formatAuthorName(article.getAuthor())).append(". ");
        }
        if (article.getTitle() != null && !article.getTitle().trim().isEmpty()) {
            citation.append('"').append(article.getTitle()).append("." + '"' + " ");
        }
        if (article.getJournal() != null && !article.getJournal().trim().isEmpty()) {
            citation.append(article.getJournal());
            if (article.getVolume() != null && !article.getVolume().trim().isEmpty()) {
                citation.append(", vol. ").append(article.getVolume());
            }
            if (article.getIssue() != null && !article.getIssue().trim().isEmpty()) {
                citation.append(", no. ").append(article.getIssue());
            }
            if (article.getPublicationYear() != null) {
                citation.append(", ").append(article.getPublicationYear());
            }
            citation.append(".");
        }
        return citation.toString().trim();
    }

    private String formatAuthorName(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }

        String[] parts = author.trim().split("\\s+");
        if (parts.length >= 2) {
            String firstName = parts[0];
            String lastName = parts[parts.length - 1];
            return lastName + ", " + firstName;
        }
        return author;
    }
}
