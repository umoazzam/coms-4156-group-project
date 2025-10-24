package com.columbia.coms4156.citationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for citation generation endpoints.
 */
public class CitationResponse {

    /**
     * The unique identifier for the citation.
     */
    @JsonProperty("CitationID")
    private String citationId;

    /**
     * The formatted citation string.
     */
    @JsonProperty("CitationString")
    private String citationString;

    /**
     * Default constructor for CitationResponse.
     */
    public CitationResponse() { }

    /**
     * Constructor for CitationResponse with citation details.
     *
     * @param citationIdParam the unique identifier for the citation
     * @param citationStringParam the formatted citation string
     */
    public CitationResponse(String citationIdParam, String citationStringParam) {
        this.citationId = citationIdParam;
        this.citationString = citationStringParam;
    }

    /**
     * Gets the citation ID.
     *
     * @return the citation ID
     */
    public String getCitationId() {
        return citationId;
    }

    /**
     * Sets the citation ID.
     *
     * @param citationIdParam the citation ID to set
     * @throws IllegalArgumentException if citationIdParam is blank
     */
    public void setCitationId(String citationIdParam) {
        if (citationIdParam != null && citationIdParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Citation ID cannot be blank");
        }
        this.citationId = citationIdParam;
    }

    /**
     * Gets the citation string.
     *
     * @return the citation string
     */
    public String getCitationString() {
        return citationString;
    }

    /**
     * Sets the citation string.
     *
     * @param citationStringParam the citation string to set
     * @throws IllegalArgumentException if citationStringParam is blank
     */
    public void setCitationString(String citationStringParam) {
        if (citationStringParam != null && citationStringParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Citation string cannot be blank");
        }
        this.citationString = citationStringParam;
    }

    @Override
    public String toString() {
        return "CitationResponse{"
                + "citationId='" + citationId + '\''
                + ", citationString='" + citationString + '\''
                + '}';
    }
}

