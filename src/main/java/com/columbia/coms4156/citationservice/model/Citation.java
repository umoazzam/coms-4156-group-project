package com.columbia.coms4156.citationservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Citation entity representing a single citation within a Submission.
 * Instead of a strict FK to a specific Media table, this stores mediaId and mediaType
 * so backend logic can resolve which media table to query (Book/Video/Article).
 */
@Entity
@Table(name = "citations")
public class Citation {

    /**
     * The unique identifier for the citation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The submission that contains this citation.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    @JsonBackReference
    private Submission submission;

    /**
     * JSON string containing user-provided metadata for the citation.
     */
    @Lob
    @Column(name = "user_input_metadata", columnDefinition = "TEXT")
    private String userInputMetaData; // JSON string provided by user

    /**
     * The ID of the media record (Book/Video/Article).
     */
    @Column(name = "media_id")
    private Long mediaId; // id of the media record (Book/Video/Article)

    /**
     * The type of media (e.g., "book", "video", "article").
     */
    @Column(name = "media_type")
    private String mediaType; // e.g. "book", "video", "article"

    /**
     * Default constructor for Citation.
     */
    public Citation() { }

    /**
     * Constructor for Citation with all required fields.
     *
     * @param submissionParam the submission containing this citation
     * @param userInputMetaDataParam the user-provided metadata
     * @param mediaIdParam the media ID
     * @param mediaTypeParam the media type
     */
    public Citation(Submission submissionParam, String userInputMetaDataParam,
                   Long mediaIdParam, String mediaTypeParam) {
        this.submission = submissionParam;
        this.userInputMetaData = userInputMetaDataParam;
        this.mediaId = mediaIdParam;
        this.mediaType = mediaTypeParam;
    }

    /**
     * Gets the citation ID.
     *
     * @return the citation ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the citation ID.
     *
     * @param idParam the citation ID to set
     * @throws IllegalArgumentException if idParam is negative
     */
    public void setId(Long idParam) {
        if (idParam != null && idParam < 0) {
            throw new IllegalArgumentException("ID cannot be negative");
        }
        this.id = idParam;
    }

    /**
     * Gets the submission containing this citation.
     *
     * @return the submission
     */
    public Submission getSubmission() {
        return submission;
    }

    /**
     * Sets the submission containing this citation.
     *
     * @param submissionParam the submission to set (can be null to clear the relationship)
     */
    public void setSubmission(Submission submissionParam) {
        this.submission = submissionParam;
    }

    /**
     * Gets the user input metadata.
     *
     * @return the user input metadata
     */
    public String getUserInputMetaData() {
        return userInputMetaData;
    }

    /**
     * Sets the user input metadata.
     *
     * @param userInputMetaDataParam the user input metadata to set
     * @throws IllegalArgumentException if userInputMetaDataParam is blank
     */
    public void setUserInputMetaData(String userInputMetaDataParam) {
        if (userInputMetaDataParam != null && userInputMetaDataParam.trim().isEmpty()) {
            throw new IllegalArgumentException("User input metadata cannot be blank");
        }
        this.userInputMetaData = userInputMetaDataParam;
    }

    /**
     * Gets the media ID.
     *
     * @return the media ID
     */
    public Long getMediaId() {
        return mediaId;
    }

    /**
     * Sets the media ID.
     *
     * @param mediaIdParam the media ID to set
     * @throws IllegalArgumentException if mediaIdParam is negative
     */
    public void setMediaId(Long mediaIdParam) {
        if (mediaIdParam != null && mediaIdParam < 0) {
            throw new IllegalArgumentException("Media ID cannot be negative");
        }
        this.mediaId = mediaIdParam;
    }

    /**
     * Gets the media type.
     *
     * @return the media type
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * Sets the media type.
     *
     * @param mediaTypeParam the media type to set
     * @throws IllegalArgumentException if mediaTypeParam is blank
     */
    public void setMediaType(String mediaTypeParam) {
        if (mediaTypeParam != null && mediaTypeParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Media type cannot be blank");
        }
        this.mediaType = mediaTypeParam;
    }

    @Override
    public String toString() {
        return "Citation{"
                + "id=" + id
                + ", submissionId=" + (submission != null ? submission.getId() : null)
                + ", mediaId=" + mediaId
                + ", mediaType='" + mediaType + '\''
                + '}';
    }
}

