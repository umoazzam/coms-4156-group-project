package com.columbia.coms4156.citationservice.repository;

import com.columbia.coms4156.citationservice.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    /**
     * Finds all submissions associated with the specified user ID.
     *
     * @param userId the user ID to search for
     * @return a list of submissions for the specified user
     */
    List<Submission> findByUserId(Long userId);
}

