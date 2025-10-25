package com.columbia.coms4156.citationservice.repository;

import com.columbia.coms4156.citationservice.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    /**
     * Finds a video by title and author, case-insensitively.
     *
     * @param title  the title of the video
     * @param author the author of the video
     * @return an Optional containing the found video, or empty if no video was found
     */
    Optional<Video> findByTitleIgnoreCaseAndAuthorIgnoreCase(String title, String author);
}
