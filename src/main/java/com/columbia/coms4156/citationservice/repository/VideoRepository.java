package com.columbia.coms4156.citationservice.repository;

import com.columbia.coms4156.citationservice.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    /**
     * Finds all videos by the specified author.
     *
     * @param author the author name to search for
     * @return a list of videos by the specified author
     */
    List<Video> findByAuthor(String author);

    /**
     * Finds all videos whose title contains the specified text (case-insensitive).
     *
     * @param title the title text to search for
     * @return a list of videos whose titles contain the specified text
     */
    List<Video> findByTitleContainingIgnoreCase(String title);
}

