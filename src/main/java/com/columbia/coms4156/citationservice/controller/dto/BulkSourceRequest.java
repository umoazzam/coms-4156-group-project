package com.columbia.coms4156.citationservice.controller.dto;

import java.util.List;

/**
 * DTO representing a bulk source request.
 *
 * <p>Contains user information and a list of sources to be processed.</p>
 */
public class BulkSourceRequest {
    /** The user associated with the bulk source request. */
    private UserDTO user;

    /** The list of sources to be processed. */
    private List<SourceDTO> sources;

    /**
     * Default constructor for BulkSourceRequest.
     */
    public BulkSourceRequest() {}

    /**
     * Gets the user associated with the request.
     *
     * @return the user
     */
    public UserDTO getUser() {
        return this.user;
    }

    /**
     * Sets the user associated with the request.
     *
     * @param user the user to set
     */
    public void setUser(UserDTO user) {
        this.user = user;
    }

    /**
     * Gets the list of sources to be processed.
     *
     * @return the list of sources
     */
    public List<SourceDTO> getSources() {
        return this.sources;
    }

    /**
     * Sets the list of sources to be processed.
     *
     * @param sources the list of sources to set
     */
    public void setSources(List<SourceDTO> sources) {
        this.sources = sources;
    }
}
