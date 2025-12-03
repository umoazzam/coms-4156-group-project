package com.columbia.coms4156.citationservice.controller.dto;

import java.util.List;
import java.util.ArrayList;

/**
 * DTO representing a batch response for sources.
 *
 * <p>Contains the submission ID, a list of citation IDs, and optional errors.</p>
 */
public class SourceBatchResponse {
    /** The ID of the submission associated with the sources. */
    private Long submissionId;

    /** The list of citation IDs. */
    private List<String> citationIds;

    /** Optional list of error messages produced while processing the batch. */
    private List<String> errors;

    /**
     * Default constructor for SourceBatchResponse.
     */
    public SourceBatchResponse() {
        this.errors = new ArrayList<>();
    }

    /**
     * Constructs a SourceBatchResponse with the given submission ID and citation IDs.
     * Keeps backward compatibility with existing callers.
     *
     * @param submissionIdParam the ID of the submission
     * @param citationIdsParam the list of citation IDs
     */
    public SourceBatchResponse(Long submissionIdParam, List<String> citationIdsParam) {
        this.submissionId = submissionIdParam;
        this.citationIds = citationIdsParam;
        this.errors = new ArrayList<>();
    }

    /**
     * Constructs a SourceBatchResponse with submission ID, citation IDs and errors.
     *
     * @param submissionIdParam the ID of the submission
     * @param citationIdsParam the list of citation IDs
     * @param errorsParam the list of error messages
     */
    public SourceBatchResponse(Long submissionIdParam, List<String> citationIdsParam,
                               List<String> errorsParam) {
        this.submissionId = submissionIdParam;
        this.citationIds = citationIdsParam;
        this.errors = errorsParam == null ? new ArrayList<>() : errorsParam;
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
     * Gets the list of citation IDs.
     *
     * @return the list of citation IDs
     */
    public List<String> getCitationIds() {
        return this.citationIds;
    }

    /**
     * Sets the list of citation IDs.
     *
     * @param citationIdsParam the list of citation IDs to set
     */
    public void setCitationIds(List<String> citationIdsParam) {
        this.citationIds = citationIdsParam;
    }

    /**
     * Gets the list of error messages.
     *
     * @return the list of errors
     */
    public List<String> getErrors() {
        return this.errors;
    }

    /**
     * Sets the list of error messages.
     *
     * @param errorsParam the list of errors to set
     */
    public void setErrors(List<String> errorsParam) {
        this.errors = errorsParam == null ? new ArrayList<>() : errorsParam;
    }
}
