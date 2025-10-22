package com.columbia.coms4156.citationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The username for the user account.
     */
    @NotBlank(message = "username is required")
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * The password for the user account.
     */
    @NotBlank(message = "password is required")
    @Column(nullable = false)
    @JsonIgnore // never serialize password in API responses
    private String password;

    /**
     * The list of submissions created by this user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Submission> submissions = new ArrayList<>();

    /**
     * Default constructor for User.
     */
    public User() { }

    /**
     * Constructor for User with username and password.
     *
     * @param usernameParam the username
     * @param passwordParam the password
     */
    public User(String usernameParam, String passwordParam) {
        this.username = usernameParam;
        this.password = passwordParam;
    }

    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the user ID.
     *
     * @param idParam the user ID to set
     */
    public void setId(Long idParam) {
        this.id = idParam;
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param usernameParam the username to set
     */
    public void setUsername(String usernameParam) {
        this.username = usernameParam;
    }

    /**
     * Sets the password.
     *
     * @param passwordParam the password to set
     */
    public void setPassword(String passwordParam) {
        this.password = passwordParam;
    }

    /**
     * Gets the list of submissions.
     *
     * @return the list of submissions
     */
    public List<Submission> getSubmissions() {
        return submissions;
    }

    /**
     * Sets the list of submissions.
     *
     * @param submissionsParam the list of submissions to set
     */
    public void setSubmissions(List<Submission> submissionsParam) {
        this.submissions = submissionsParam;
    }

    /**
     * Adds a submission to this user.
     *
     * @param submission the submission to add
     */
    public void addSubmission(Submission submission) {
        if (submission == null) {
            return;
        }
        submissions.add(submission);
        submission.setUser(this);
    }

    /**
     * Removes a submission from this user.
     *
     * @param submission the submission to remove
     */
    public void removeSubmission(Submission submission) {
        if (submission == null) {
            return;
        }
        submissions.remove(submission);
        submission.setUser(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", submissionsCount=" + (submissions != null ? submissions.size() : 0)
                + '}';
    }
}

