package com.columbia.coms4156.citationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * Response DTO for group citation generation endpoint.
 */
public class GroupCitationResponse {

    /**
     * The submission ID for the group of citations.
     */
    @JsonProperty("submissionId")
    private Long submissionId;

    /**
     * Map of citation IDs to citation strings.
     */
    @JsonProperty("Citations")
    private Map<String, String> citations; // CitationID -> CitationString mapping

    /**
     * Default constructor for GroupCitationResponse.
     * Required for JSON deserialization.
     */
    public GroupCitationResponse() {
        // Empty constructor required for JSON deserialization
    }

    /**
     * Constructor for GroupCitationResponse with submission details.
     *
     * @param submissionIdParam the submission ID
     * @param citationsParam the map of citations
     */
    public GroupCitationResponse(Long submissionIdParam, Map<String, String> citationsParam) {
        this.submissionId = submissionIdParam;
        this.citations = citationsParam;
    }

    /**
     * Gets the submission ID.
     *
     * @return the submission ID
     */
    public Long getSubmissionId() {
        return submissionId;
    }

    /**
     * Sets the submission ID.
     *
     * @param submissionIdParam the submission ID to set
     * @throws IllegalArgumentException if submissionIdParam is negative
     */
    public void setSubmissionId(Long submissionIdParam) {
        if (submissionIdParam != null && submissionIdParam < 0) {
            throw new IllegalArgumentException("Submission ID cannot be negative");
        }
        this.submissionId = submissionIdParam;
    }

    /**
     * Gets the citations map.
     *
     * @return the citations map
     */
    public Map<String, String> getCitations() {
        return citations;
    }

    /**
     * Sets the citations map.
     *
     * @param citationsParam the citations map to set
     * @throws IllegalArgumentException if citationsParam is null
     */
    public void setCitations(Map<String, String> citationsParam) {
        if (citationsParam == null) {
            throw new IllegalArgumentException("Citations map cannot be null");
        }
        this.citations = citationsParam;
    }

    @Override
    public String toString() {
        return "GroupCitationResponse{"
                + "submissionId=" + submissionId
                + ", citations=" + citations
                + '}';
    }
}

