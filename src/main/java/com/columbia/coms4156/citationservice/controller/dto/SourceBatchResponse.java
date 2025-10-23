package com.columbia.coms4156.citationservice.controller.dto;

import java.util.List;

/**
 * DTO representing a batch response for sources.
 *
 * <p>Contains the submission ID and a list of source IDs.</p>
 */
public class SourceBatchResponse {
    /** The ID of the submission associated with the sources. */
    private Long submissionId;

    /** The list of source IDs. */
    private List<String> sourceIds;

    /**
     * Default constructor for SourceBatchResponse.
     */
    public SourceBatchResponse() { }

    /**
     * Constructs a SourceBatchResponse with the given submission ID and source IDs.
     *
     * @param submissionIdParam the ID of the submission
     * @param sourceIdsParam the list of source IDs
     */
    public SourceBatchResponse(Long submissionIdParam, List<String> sourceIdsParam) {
        this.submissionId = submissionIdParam;
        this.sourceIds = sourceIdsParam;
    }

    /**
     * Gets the submission ID.
     *
     * @return the submission ID
     */
    public Long getSubmissionId() {
        return this.submissionId;
    }

    /**
     * Sets the submission ID.
     *
     * @param submissionIdParam the submission ID to set
     */
    public void setSubmissionId(Long submissionIdParam) {
        this.submissionId = submissionIdParam;
    }

    /**
     * Gets the list of source IDs.
     *
     * @return the list of source IDs
     */
    public List<String> getSourceIds() {
        return this.sourceIds;
    }

    /**
     * Sets the list of source IDs.
     *
     * @param sourceIdsParam the list of source IDs to set
     */
    public void setSourceIds(List<String> sourceIdsParam) {
        this.sourceIds = sourceIdsParam;
    }
}
