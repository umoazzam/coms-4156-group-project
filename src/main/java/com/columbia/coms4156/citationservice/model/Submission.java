package com.columbia.coms4156.citationservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Submission entity representing a user's citation submission (a collection of citations).
 */
@Entity
@Table(name = "submissions")
public class Submission {

    /**
     * The unique identifier for the submission.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who created this submission.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    /**
     * The date and time when the submission was created.
     */
    @Column
    private LocalDateTime date;

    /**
     * The citation format (e.g., MLA, APA, Chicago).
     */
    @Column
    private String format; // e.g. MLA

    /**
     * The list of citations in this submission.
     */
    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Citation> citations = new ArrayList<>();

    /**
     * Default constructor for Submission.
     */
    public Submission() {
        this.date = LocalDateTime.now();
    }

    /**
     * Constructor for Submission with user and format.
     *
     * @param userParam the user creating the submission
     * @param formatParam the citation format
     */
    public Submission(User userParam, String formatParam) {
        this.user = userParam;
        this.format = formatParam;
        this.date = LocalDateTime.now();
    }

    /**
     * Gets the submission ID.
     *
     * @return the submission ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the submission ID.
     *
     * @param idParam the submission ID to set
     */
    public void setId(Long idParam) {
        this.id = idParam;
    }

    /**
     * Gets the user who created the submission.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who created the submission.
     *
     * @param userParam the user to set
     */
    public void setUser(User userParam) {
        this.user = userParam;
    }

    /**
     * Gets the submission date.
     *
     * @return the submission date
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Sets the submission date.
     *
     * @param dateParam the submission date to set
     */
    public void setDate(LocalDateTime dateParam) {
        this.date = dateParam;
    }

    /**
     * Gets the citation format.
     *
     * @return the citation format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the citation format.
     *
     * @param formatParam the citation format to set
     */
    public void setFormat(String formatParam) {
        this.format = formatParam;
    }

    /**
     * Gets the list of citations.
     *
     * @return the list of citations
     */
    public List<Citation> getCitations() {
        return citations;
    }

    /**
     * Sets the list of citations.
     *
     * @param citationsParam the list of citations to set
     */
    public void setCitations(List<Citation> citationsParam) {
        this.citations = citationsParam;
    }

    /**
     * Adds a citation to this submission.
     *
     * @param citation the citation to add
     */
    public void addCitation(Citation citation) {
        citations.add(citation);
        citation.setSubmission(this);
    }

    /**
     * Removes a citation from this submission.
     *
     * @param citation the citation to remove
     */
    public void removeCitation(Citation citation) {
        citations.remove(citation);
        citation.setSubmission(null);
    }

    @Override
    public String toString() {
        return "Submission{"
                + "id=" + id
                + ", userId=" + (user != null ? user.getId() : null)
                + ", date=" + date
                + ", format='" + format + '\''
                + ", citationsCount=" + (citations != null ? citations.size() : 0)
                + '}';
    }
}

