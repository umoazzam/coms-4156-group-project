package com.columbia.coms4156.citationservice.repository;

import com.columbia.coms4156.citationservice.model.Citation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CitationRepository extends JpaRepository<Citation, Long> {
    /**
     * Finds all citations associated with the specified submission ID.
     *
     * @param submissionId the submission ID to search for
     * @return a list of citations for the specified submission
     */
    List<Citation> findBySubmissionId(Long submissionId);

    /**
     * Finds an existing citation for a given submission, media id and media type.
     *
     * @param submissionId the submission ID to search for
     * @param mediaId the media ID to search for
     * @param mediaType the media type to search for
     * @return an optional containing the citation if found, or empty if not found
     */
    Optional<Citation> findBySubmissionIdAndMediaIdAndMediaType(
        Long submissionId, Long mediaId, String mediaType);
}
