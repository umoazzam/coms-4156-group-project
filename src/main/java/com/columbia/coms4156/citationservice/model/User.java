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
     * Minimum username length.
     */
    private static final int MIN_USERNAME_LENGTH = 3;

    /**
     * Maximum username length.
     */
    private static final int MAX_USERNAME_LENGTH = 50;

    /**
     * Minimum password length.
     */
    private static final int MIN_PASSWORD_LENGTH = 8;

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
     * Required by JPA for entity instantiation.
     */
    public User() {
        // Empty constructor required by JPA
    }

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
     * @throws IllegalArgumentException if usernameParam is null, blank, or invalid format
     */
    public void setUsername(String usernameParam) {
        if (usernameParam == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if (usernameParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (!isValidUsername(usernameParam)) {
            throw new IllegalArgumentException("Username must be 3-50 characters and contain only "
                    + "letters, numbers, underscores, and hyphens");
        }
        this.username = usernameParam;
    }

    /**
     * Sets the password.
     *
     * @param passwordParam the password to set
     * @throws IllegalArgumentException if passwordParam is null, blank, or doesn't meet
     *                                  security requirements
     */
    public void setPassword(String passwordParam) {
        if (passwordParam == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        if (passwordParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be blank");
        }
        if (!isValidPassword(passwordParam)) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and "
                    + "contain at least one uppercase letter, one lowercase letter, and one digit");
        }
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
     * @throws IllegalArgumentException if submissionsParam is null
     */
    public void setSubmissions(List<Submission> submissionsParam) {
        if (submissionsParam == null) {
            throw new IllegalArgumentException("Submissions list cannot be null");
        }
        this.submissions = submissionsParam;
    }

    /**
     * Adds a submission to this user.
     *
     * @param submission the submission to add
     * @throws IllegalArgumentException if submission is null or already belongs to another user
     */
    public void addSubmission(Submission submission) {
        if (submission == null) {
            throw new IllegalArgumentException("Submission cannot be null");
        }
        if (submission.getUser() != null && !submission.getUser().equals(this)) {
            throw new IllegalArgumentException("Submission already belongs to another user");
        }
        if (!submissions.contains(submission)) {
            submissions.add(submission);
            submission.setUser(this);
        }
    }

    /**
     * Removes a submission from this user.
     *
     * @param submission the submission to remove
     * @throws IllegalArgumentException if submission is null
     */
    public void removeSubmission(Submission submission) {
        if (submission == null) {
            throw new IllegalArgumentException("Submission cannot be null");
        }
        if (submissions.remove(submission)) {
            submission.setUser(null);
        }
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

    /**
     * Validates username format and length.
     * Username must be 3-50 characters and contain only letters, numbers, underscores, and hyphens.
     *
     * @param usernameValue the username to validate
     * @return true if valid username format, false otherwise
     */
    private boolean isValidUsername(String usernameValue) {
        if (usernameValue == null || usernameValue.trim().isEmpty()) {
            return false;
        }

        String trimmedUsername = usernameValue.trim();

        // Check length (3-50 characters)
        if (trimmedUsername.length() < MIN_USERNAME_LENGTH
                || trimmedUsername.length() > MAX_USERNAME_LENGTH) {
            return false;
        }

        // Check format: only letters, numbers, underscores, and hyphens
        return trimmedUsername.matches("^[a-zA-Z0-9_-]+$");
    }

    /**
     * Validates password strength and format.
     * Password must be at least 8 characters and contain uppercase, lowercase, and digit.
     *
     * @param passwordValue the password to validate
     * @return true if valid password format, false otherwise
     */
    private boolean isValidPassword(String passwordValue) {
        if (passwordValue == null || passwordValue.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;

        for (char c : passwordValue.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit;
    }
}

