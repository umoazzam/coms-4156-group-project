package com.columbia.coms4156.citationservice.repository;

import com.columbia.coms4156.citationservice.model.Citation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitationRepository extends JpaRepository<Citation, Long> {
    /**
     * Finds all citations associated with the specified submission ID.
     *
     * @param submissionId the submission ID to search for
     * @return a list of citations for the specified submission
     */
    List<Citation> findBySubmissionId(Long submissionId);
}
