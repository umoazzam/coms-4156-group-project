package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.model.*;
import com.columbia.coms4156.citationservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private CitationRepository citationRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

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

    /**
     * Generate a citation for a single source by sourceId with specified style and backfill option
     */
    public CitationResponse generateCitationForSource(Long sourceId, String style, boolean backfill) {
        // Find the citation record
        Optional<Citation> citationOpt = citationRepository.findById(sourceId);
        if (citationOpt.isEmpty()) {
            throw new IllegalArgumentException("Citation not found with ID: " + sourceId);
        }

        Citation citation = citationOpt.get();
        String citationString = generateCitationByMediaType(citation.getMediaId(), citation.getMediaType(), style, backfill);
        
        return new CitationResponse(sourceId.toString(), citationString);
    }

    /**
     * Generate citations for all sources in a submission group
     */
    public GroupCitationResponse generateCitationsForGroup(Long submissionId, String style, boolean backfill) {
        Optional<Submission> submissionOpt = submissionRepository.findById(submissionId);
        if (submissionOpt.isEmpty()) {
            throw new IllegalArgumentException("Submission not found with ID: " + submissionId);
        }

        Submission submission = submissionOpt.get();
        Map<String, String> citations = new HashMap<>();

        for (Citation citation : submission.getCitations()) {
            String citationString = generateCitationByMediaType(citation.getMediaId(), citation.getMediaType(), style, backfill);
            citations.put(citation.getId().toString(), citationString);
        }

        return new GroupCitationResponse(submissionId, citations);
    }

    /**
     * Generate citation based on media type and style
     */
    private String generateCitationByMediaType(Long mediaId, String mediaType, String style, boolean backfill) {
        switch (mediaType.toLowerCase()) {
            case "book":
                Optional<Book> book = bookRepository.findById(mediaId);
                if (book.isPresent()) {
                    return generateCitationByStyle(book.get(), style);
                }
                break;
            case "video":
                Optional<Video> video = videoRepository.findById(mediaId);
                if (video.isPresent()) {
                    return generateCitationByStyle(video.get(), style);
                }
                break;
            case "article":
                Optional<Article> article = articleRepository.findById(mediaId);
                if (article.isPresent()) {
                    return generateCitationByStyle(article.get(), style);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported media type: " + mediaType);
        }
        throw new IllegalArgumentException("Media not found with ID: " + mediaId);
    }

    /**
     * Generate citation by style for any source type
     */
    private String generateCitationByStyle(Object source, String style) {
        switch (style.toUpperCase()) {
            case "MLA":
                if (source instanceof Book) {
                    return generateMLACitation((Book) source);
                } else if (source instanceof Video) {
                    return generateMLACitation((Video) source);
                } else if (source instanceof Article) {
                    return generateMLACitation((Article) source);
                }
                break;
            case "APA":
                if (source instanceof Book) {
                    return generateAPACitation((Book) source);
                } else if (source instanceof Video) {
                    return generateAPACitation((Video) source);
                } else if (source instanceof Article) {
                    return generateAPACitation((Article) source);
                }
                break;
            case "CHICAGO":
                if (source instanceof Book) {
                    return generateChicagoCitation((Book) source);
                } else if (source instanceof Video) {
                    return generateChicagoCitation((Video) source);
                } else if (source instanceof Article) {
                    return generateChicagoCitation((Article) source);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported citation style: " + style);
        }
        throw new IllegalArgumentException("Unsupported source type: " + source.getClass().getSimpleName());
    }

    // APA CITATION METHODS
    public String generateAPACitation(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        StringBuilder citation = new StringBuilder();

        if (book.getAuthor() != null && !book.getAuthor().trim().isEmpty()) {
            citation.append(formatAPAAuthorName(book.getAuthor())).append(" ");
        }

        if (book.getPublicationYear() != null) {
            citation.append("(").append(book.getPublicationYear()).append("). ");
        }

        if (book.getTitle() != null && !book.getTitle().trim().isEmpty()) {
            citation.append(book.getTitle()).append(". ");
        }

        if (book.getPublisher() != null && !book.getPublisher().trim().isEmpty()) {
            citation.append(book.getPublisher());
            if (book.getCity() != null && !book.getCity().trim().isEmpty()) {
                citation.append(", ").append(book.getCity());
            }
            citation.append(".");
        }

        return citation.toString().trim();
    }

    public String generateAPACitation(Video video) {
        if (video == null) {
            throw new IllegalArgumentException("Video cannot be null");
        }

        StringBuilder citation = new StringBuilder();

        if (video.getAuthor() != null && !video.getAuthor().trim().isEmpty()) {
            citation.append(formatAPAAuthorName(video.getAuthor())).append(" ");
        }

        if (video.getReleaseYear() != null) {
            citation.append("(").append(video.getReleaseYear()).append("). ");
        }

        if (video.getTitle() != null && !video.getTitle().trim().isEmpty()) {
            citation.append(video.getTitle()).append(" [Video]. ");
        }

        if (video.getPlatform() != null && !video.getPlatform().trim().isEmpty()) {
            citation.append(video.getPlatform()).append(".");
        }

        return citation.toString().trim();
    }

    public String generateAPACitation(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("Article cannot be null");
        }

        StringBuilder citation = new StringBuilder();

        if (article.getAuthor() != null && !article.getAuthor().trim().isEmpty()) {
            citation.append(formatAPAAuthorName(article.getAuthor())).append(" ");
        }

        if (article.getPublicationYear() != null) {
            citation.append("(").append(article.getPublicationYear()).append("). ");
        }

        if (article.getTitle() != null && !article.getTitle().trim().isEmpty()) {
            citation.append(article.getTitle()).append(". ");
        }

        if (article.getJournal() != null && !article.getJournal().trim().isEmpty()) {
            citation.append(article.getJournal());
            if (article.getVolume() != null && !article.getVolume().trim().isEmpty()) {
                citation.append(", ").append(article.getVolume());
            }
            if (article.getIssue() != null && !article.getIssue().trim().isEmpty()) {
                citation.append("(").append(article.getIssue()).append(")");
            }
            citation.append(".");
        }

        return citation.toString().trim();
    }

    // CHICAGO CITATION METHODS
    public String generateChicagoCitation(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        StringBuilder citation = new StringBuilder();

        if (book.getAuthor() != null && !book.getAuthor().trim().isEmpty()) {
            citation.append(formatAuthorName(book.getAuthor())).append(". ");
        }

        if (book.getTitle() != null && !book.getTitle().trim().isEmpty()) {
            citation.append("\"").append(book.getTitle()).append(".\" ");
        }

        if (book.getCity() != null && !book.getCity().trim().isEmpty()) {
            citation.append(book.getCity());
            if (book.getPublisher() != null && !book.getPublisher().trim().isEmpty()) {
                citation.append(": ").append(book.getPublisher());
            }
            citation.append(", ");
        }

        if (book.getPublicationYear() != null) {
            citation.append(book.getPublicationYear()).append(".");
        }

        return citation.toString().trim();
    }

    public String generateChicagoCitation(Video video) {
        if (video == null) {
            throw new IllegalArgumentException("Video cannot be null");
        }

        StringBuilder citation = new StringBuilder();

        if (video.getAuthor() != null && !video.getAuthor().trim().isEmpty()) {
            citation.append(formatAuthorName(video.getAuthor())).append(". ");
        }

        if (video.getTitle() != null && !video.getTitle().trim().isEmpty()) {
            citation.append("\"").append(video.getTitle()).append(".\" ");
        }

        if (video.getPlatform() != null && !video.getPlatform().trim().isEmpty()) {
            citation.append(video.getPlatform()).append(", ");
        }

        if (video.getReleaseYear() != null) {
            citation.append(video.getReleaseYear()).append(".");
        }

        return citation.toString().trim();
    }

    public String generateChicagoCitation(Article article) {
        if (article == null) {
            throw new IllegalArgumentException("Article cannot be null");
        }

        StringBuilder citation = new StringBuilder();

        if (article.getAuthor() != null && !article.getAuthor().trim().isEmpty()) {
            citation.append(formatAuthorName(article.getAuthor())).append(". ");
        }

        if (article.getTitle() != null && !article.getTitle().trim().isEmpty()) {
            citation.append("\"").append(article.getTitle()).append(".\" ");
        }

        if (article.getJournal() != null && !article.getJournal().trim().isEmpty()) {
            citation.append(article.getJournal());
            if (article.getVolume() != null && !article.getVolume().trim().isEmpty()) {
                citation.append(" ").append(article.getVolume());
            }
            if (article.getIssue() != null && !article.getIssue().trim().isEmpty()) {
                citation.append(", no. ").append(article.getIssue());
            }
            citation.append(" (");
        }

        if (article.getPublicationYear() != null) {
            citation.append(article.getPublicationYear()).append(").");
        }

        return citation.toString().trim();
    }

    private String formatAPAAuthorName(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }

        String[] parts = author.trim().split("\\s+");
        if (parts.length >= 2) {
            String firstName = parts[0];
            String lastName = parts[parts.length - 1];
            return lastName + ", " + firstName.charAt(0) + ".";
        }
        return author;
    }
}
