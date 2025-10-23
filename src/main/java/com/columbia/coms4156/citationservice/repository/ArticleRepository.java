package com.columbia.coms4156.citationservice.repository;

import com.columbia.coms4156.citationservice.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    /**
     * Finds an article by title and author, case-insensitively.
     *
     * @param title  the title of the article
     * @param author the author of the article
     * @return an Optional containing the found article, or empty if no article was found
     */
    Optional<Article> findByTitleIgnoreCaseAndAuthorIgnoreCase(String title, String author);
}
