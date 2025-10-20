package com.columbia.coms4156.citationservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Submission entity representing a user's citation submission (a collection of citations)
 */
@Entity
@Table(name = "submissions")
public class Submission {

    // Instance Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column
    private LocalDateTime date;

    @Column
    private String format; // e.g. MLA

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Citation> citations = new ArrayList<>();

    // Constructors
    public Submission() {
        this.date = LocalDateTime.now();
    }

    public Submission(User user, String format) {
        this.user = user;
        this.format = format;
        this.date = LocalDateTime.now();
    }

    // Methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<Citation> getCitations() {
        return citations;
    }

    public void setCitations(List<Citation> citations) {
        this.citations = citations;
    }

    public void addCitation(Citation citation) {
        citations.add(citation);
        citation.setSubmission(this);
    }

    public void removeCitation(Citation citation) {
        citations.remove(citation);
        citation.setSubmission(null);
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : null) +
                ", date=" + date +
                ", format='" + format + '\'' +
                ", citationsCount=" + (citations != null ? citations.size() : 0) +
                '}';
    }
}

