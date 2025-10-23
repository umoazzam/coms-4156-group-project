package com.columbia.coms4156.citationservice.controller.dto;

/**
 * DTO representing a user.
 *
 * <p>Contains user-related information such as username.</p>
 */
public class UserDTO {
    /** The username of the user. */
    private String username;

    /**
     * Default constructor for UserDTO.
     */
    public UserDTO() {}

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the username.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
