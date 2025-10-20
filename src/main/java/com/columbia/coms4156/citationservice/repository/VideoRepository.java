package com.columbia.coms4156.citationservice.repository;

import com.columbia.coms4156.citationservice.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByAuthor(String author);
    List<Video> findByTitleContainingIgnoreCase(String title);
}

