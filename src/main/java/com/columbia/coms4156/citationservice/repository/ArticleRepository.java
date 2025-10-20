package com.columbia.coms4156.citationservice.repository;

import com.columbia.coms4156.citationservice.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByAuthor(String author);
    List<Article> findByTitleContainingIgnoreCase(String title);
}
