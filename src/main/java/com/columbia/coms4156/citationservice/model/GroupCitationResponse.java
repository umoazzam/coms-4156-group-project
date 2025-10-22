package com.columbia.coms4156.citationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * Response DTO for group citation generation endpoint.
 */
public class GroupCitationResponse {

    @JsonProperty("submissionId")
    private Long submissionId;

    @JsonProperty("Citations")
    private Map<String, String> citations; // CitationID -> CitationString mapping

    // Constructors
    public GroupCitationResponse() {}

    public GroupCitationResponse(Long submissionId, Map<String, String> citations) {
        this.submissionId = submissionId;
        this.citations = citations;
    }

    // Getters and Setters
    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public Map<String, String> getCitations() {
        return citations;
    }

    public void setCitations(Map<String, String> citations) {
        this.citations = citations;
    }

    @Override
    public String toString() {
        return "GroupCitationResponse{" +
                "submissionId=" + submissionId +
                ", citations=" + citations +
                '}';
    }
}

