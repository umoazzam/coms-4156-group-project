package com.columbia.coms4156.citationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for citation generation endpoints.
 */
public class CitationResponse {

    @JsonProperty("CitationID")
    private String citationId;

    @JsonProperty("CitationString")
    private String citationString;

    // Constructors
    public CitationResponse() {}

    public CitationResponse(String citationId, String citationString) {
        this.citationId = citationId;
        this.citationString = citationString;
    }

    // Getters and Setters
    public String getCitationId() {
        return citationId;
    }

    public void setCitationId(String citationId) {
        this.citationId = citationId;
    }

    public String getCitationString() {
        return citationString;
    }

    public void setCitationString(String citationString) {
        this.citationString = citationString;
    }

    @Override
    public String toString() {
        return "CitationResponse{" +
                "citationId='" + citationId + '\'' +
                ", citationString='" + citationString + '\'' +
                '}';
    }
}

