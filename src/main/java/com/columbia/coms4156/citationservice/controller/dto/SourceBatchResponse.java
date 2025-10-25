package com.columbia.coms4156.citationservice.controller.dto;

import java.util.List;
import java.util.ArrayList;

/**
 * DTO representing a batch response for sources.
 *
 * <p>Contains the submission ID, a list of source IDs, and optional errors.</p>
 */
public class SourceBatchResponse {
    /** The ID of the submission associated with the sources. */
    private Long submissionId;

    /** The list of source IDs. */
    private List<String> sourceIds;

    /** Optional list of error messages produced while processing the batch. */
    private List<String> errors;

    /**
     * Default constructor for SourceBatchResponse.
     */
    public SourceBatchResponse() {
        this.errors = new ArrayList<>();
    }

    /**
     * Constructs a SourceBatchResponse with the given submission ID and source IDs.
     * Keeps backward compatibility with existing callers.
     *
     * @param submissionIdParam the ID of the submission
     * @param sourceIdsParam the list of source IDs
     */
    public SourceBatchResponse(Long submissionIdParam, List<String> sourceIdsParam) {
        this.submissionId = submissionIdParam;
        this.sourceIds = sourceIdsParam;
        this.errors = new ArrayList<>();
    }

    /**
     * Constructs a SourceBatchResponse with submission ID, source IDs and errors.
     *
     * @param submissionIdParam the ID of the submission
     * @param sourceIdsParam the list of source IDs
     * @param errorsParam the list of error messages
     */
    public SourceBatchResponse(Long submissionIdParam, List<String> sourceIdsParam,
                               List<String> errorsParam) {
        this.submissionId = submissionIdParam;
        this.sourceIds = sourceIdsParam;
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
