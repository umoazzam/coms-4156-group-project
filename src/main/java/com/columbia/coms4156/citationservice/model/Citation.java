package com.columbia.coms4156.citationservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * Citation entity representing a single citation within a Submission.
 * Instead of a strict FK to a specific Media table, this stores mediaId and mediaType
 * so backend logic can resolve which media table to query (Book/Video/Article).
 */
@Entity
@Table(name = "citations")
public class Citation {

    // Instance Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    @JsonBackReference
    private Submission submission;

    @Lob
    @Column(name = "user_input_metadata", columnDefinition = "TEXT")
    private String userInputMetaData; // JSON string provided by user

    @Column(name = "media_id")
    private Long mediaId; // id of the media record (Book/Video/Article)

    @Column(name = "media_type")
    private String mediaType; // e.g. "book", "video", "article"

    // Constructors
    public Citation() {}

    public Citation(Submission submission, String userInputMetaData, Long mediaId, String mediaType) {
        this.submission = submission;
        this.userInputMetaData = userInputMetaData;
        this.mediaId = mediaId;
        this.mediaType = mediaType;
    }

    // Methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public String getUserInputMetaData() {
        return userInputMetaData;
    }

    public void setUserInputMetaData(String userInputMetaData) {
        this.userInputMetaData = userInputMetaData;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public String toString() {
        return "Citation{" +
                "id=" + id +
                ", submissionId=" + (submission != null ? submission.getId() : null) +
                ", mediaId=" + mediaId +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }
}

