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
     * @param usernameParam the username to set
     */
    public void setUsername(String usernameParam) {
        this.username = usernameParam;
    }
}
