package com.columbia.coms4156.citationservice.repository;

import com.columbia.coms4156.citationservice.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    /**
     * Finds all articles by the specified author.
     *
     * @param author the author name to search for
     * @return a list of articles by the specified author
     */
    List<Article> findByAuthor(String author);

    /**
     * Finds all articles whose title contains the specified text (case-insensitive).
     *
     * @param title the title text to search for
     * @return a list of articles whose titles contain the specified text
     */
    List<Article> findByTitleContainingIgnoreCase(String title);
}
