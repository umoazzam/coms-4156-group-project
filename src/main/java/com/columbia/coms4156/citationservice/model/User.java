package com.columbia.coms4156.citationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User entity representing an application user who can create submissions.
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_username", columnList = "username")
})
public class User {

    // Instance Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "username is required")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "password is required")
    @Column(nullable = false)
    @JsonIgnore // never serialize password in API responses
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Submission> submissions = new ArrayList<>();

    // Constructors
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Methods
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public List<Submission> getSubmissions() { return submissions; }

    public void setSubmissions(List<Submission> submissions) { this.submissions = submissions; }

    public void addSubmission(Submission submission) {
        if (submission == null) return;
        submissions.add(submission);
        submission.setUser(this);
    }

    public void removeSubmission(Submission submission) {
        if (submission == null) return;
        submissions.remove(submission);
        submission.setUser(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", submissionsCount=" + (submissions != null ? submissions.size() : 0) +
                '}';
    }
}

