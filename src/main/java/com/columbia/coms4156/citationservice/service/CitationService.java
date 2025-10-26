package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.Citation;
import com.columbia.coms4156.citationservice.model.CitationResponse;
import com.columbia.coms4156.citationservice.model.GroupCitationResponse;
import com.columbia.coms4156.citationservice.model.Submission;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.repository.ArticleRepository;
import com.columbia.coms4156.citationservice.repository.BookRepository;
import com.columbia.coms4156.citationservice.repository.CitationRepository;
import com.columbia.coms4156.citationservice.repository.SubmissionRepository;
import com.columbia.coms4156.citationservice.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for citation-related business logic and citation generation.
 * Handles CRUD operations for various citation types and citation formatting.
 * Currently supports books, videos, and articles.
 */
@Service
public class CitationService {

    /**
     * Repository for managing Book entities.
     */
    @Autowired
    private BookRepository bookRepository;

    /**
     * Repository for managing Video entities.
     */
    @Autowired
    private VideoRepository videoRepository;

    /**
     * Repository for managing Article entities.
     */
    @Autowired
    private ArticleRepository articleRepository;

    /**
     * Repository for managing Citation entities.
     */
    @Autowired
    private CitationRepository citationRepository;

    /**
     * Repository for managing Submission entities.
     */
    @Autowired
    private SubmissionRepository submissionRepository;

    // CITATION GENERATION METHODS
    /**
     * Generates an MLA format citation for a Book.
     *
     * @param book the Book to generate citation for
     * @return the MLA formatted citation string
     * @throws IllegalArgumentException if book is null
     */
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

    /**
     * Generates an MLA format citation for a Video.
     *
     * @param video the Video to generate citation for
     * @return the MLA formatted citation string
     * @throws IllegalArgumentException if video is null
     */
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
            if (video.getReleaseYear() != null) {
                citation.append(", ");
            } else {
                citation.append(". ");
            }
        }
        if (video.getReleaseYear() != null) {
            citation.append(video.getReleaseYear()).append(".");
        }
        return citation.toString().trim();
    }

    /**
     * Generates an MLA format citation for an Article.
     *
     * @param article the Article to generate citation for
     * @return the MLA formatted citation string
     * @throws IllegalArgumentException if article is null
     */
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

    /**
     * Formats an author name for citation purposes.
     *
     * @param author the author name to format
     * @return the formatted author name (Last, First)
     * @throws IllegalArgumentException if author is null or empty
     */
    public String formatAuthorName(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }

        String[] authors = author.split(",");
        StringBuilder formattedAuthors = new StringBuilder();

        for (int i = 0; i < authors.length; i++) {
            String authorName = authors[i].trim();
            String[] parts = authorName.split("\\s+");
            if (parts.length >= 2) {
                String firstName = parts[0];
                String lastName = parts[parts.length - 1];
                formattedAuthors.append(lastName).append(", ").append(firstName);
            } else {
                formattedAuthors.append(authorName);
            }

            if (i < authors.length - 1) {
                formattedAuthors.append(" and ");
            }
        }

        return formattedAuthors.toString();
    }

    /**
     * UPDATE NEEDED FOR NEW API FLOW
     * Generate a citation for a single source by sourceId with specified style and backfill option.
     *
     * @param sourceId the ID of the source to generate citation for
     * @param style the citation style (MLA, APA, CHICAGO)
     * @param backfill whether to use backfill option
     * @return CitationResponse containing the generated citation
     * @throws IllegalArgumentException if citation not found
     */
    public CitationResponse generateCitationForSource(Long sourceId, String style,
                                                     boolean backfill) {
        // Find the citation record
        Optional<Citation> citationOpt = citationRepository.findById(sourceId);
        if (citationOpt.isEmpty()) {
            throw new IllegalArgumentException("Citation not found with ID: " + sourceId);
        }

        Citation citation = citationOpt.get();
        String citationString = generateCitationByMediaType(citation.getMediaId(),
                citation.getMediaType(), style, backfill);

        return new CitationResponse(sourceId.toString(), citationString);
    }

    /**
     * UPDATE NEEDED FOR NEW API FLOW
     * Generate citations for all sources in a submission group.
     *
     * @param submissionId the ID of the submission containing sources
     * @param style the citation style (MLA, APA, CHICAGO)
     * @param backfill whether to use backfill option
     * @return GroupCitationResponse containing all generated citations
     * @throws IllegalArgumentException if submission not found
     */
    public GroupCitationResponse generateCitationsForGroup(Long submissionId, String style,
                                                          boolean backfill) {
        Optional<Submission> submissionOpt = submissionRepository.findById(submissionId);
        if (submissionOpt.isEmpty()) {
            throw new IllegalArgumentException("Submission not found with ID: " + submissionId);
        }

        Submission submission = submissionOpt.get();
        Map<String, String> citations = new HashMap<>();

        for (Citation citation : submission.getCitations()) {
            String citationString = generateCitationByMediaType(citation.getMediaId(),
                    citation.getMediaType(), style, backfill);
            citations.put(citation.getId().toString(), citationString);
        }

        return new GroupCitationResponse(submissionId, citations);
    }

    /**
     * Generate citation based on media type and style.
     *
     * @param mediaId the ID of the media item
     * @param mediaType the type of media (book, video, article)
     * @param style the citation style (MLA, APA, CHICAGO)
     * @param backfill whether to use backfill option (currently unused)
     * @return the formatted citation string
     * @throws IllegalArgumentException if media type is unsupported or media not found
     */
    private String generateCitationByMediaType(Long mediaId, String mediaType, String style,
                                             boolean backfill) {
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
     * Generate citation by style for any source type.
     *
     * @param source the source object (Book, Video, or Article)
     * @param style the citation style (MLA, APA, CHICAGO)
     * @return the formatted citation string
     * @throws IllegalArgumentException if citation style or source type is unsupported
     */
    public String generateCitationByStyle(Object source, String style) {
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
        throw new IllegalArgumentException("Unsupported source type: "
                + source.getClass().getSimpleName());
    }

    // APA CITATION METHODS
    /**
     * Generates an APA format citation for a Book.
     *
     * @param book the Book to generate citation for
     * @return the APA formatted citation string
     * @throws IllegalArgumentException if book is null
     */
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

    /**
     * Generates an APA format citation for a Video.
     *
     * @param video the Video to generate citation for
     * @return the APA formatted citation string
     * @throws IllegalArgumentException if video is null
     */
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

    /**
     * Generates an APA format citation for an Article.
     *
     * @param article the Article to generate citation for
     * @return the APA formatted citation string
     * @throws IllegalArgumentException if article is null
     */
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
    /**
     * Generates a Chicago format citation for a Book.
     *
     * @param book the Book to generate citation for
     * @return the Chicago formatted citation string
     * @throws IllegalArgumentException if book is null
     */
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

    /**
     * Generates a Chicago format citation for a Video.
     *
     * @param video the Video to generate citation for
     * @return the Chicago formatted citation string
     * @throws IllegalArgumentException if video is null
     */
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

    /**
     * Generates a Chicago format citation for an Article.
     *
     * @param article the Article to generate citation for
     * @return the Chicago formatted citation string
     * @throws IllegalArgumentException if article is null
     */
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

    /**
     * Formats an author name for APA citation style.
     *
     * @param author the author name to format
     * @return the formatted author name in APA style (Last, F.)
     * @throws IllegalArgumentException if author is null or empty
     */
    public String formatAPAAuthorName(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }

        String[] authors = author.split(",");
        StringBuilder formattedAuthors = new StringBuilder();

        for (int i = 0; i < authors.length; i++) {
            String authorName = authors[i].trim();
            String[] parts = authorName.split("\\s+");
            if (parts.length >= 2) {
                String firstName = parts[0];
                String lastName = parts[parts.length - 1];
                formattedAuthors.append(lastName).append(", ")
                        .append(firstName.charAt(0)).append(".");
            } else {
                formattedAuthors.append(authorName);
            }

            if (i < authors.length - 2) {
                formattedAuthors.append(", ");
            } else if (i == authors.length - 2) {
                formattedAuthors.append(" & ");
            }
        }

        return formattedAuthors.toString();
    }
}
