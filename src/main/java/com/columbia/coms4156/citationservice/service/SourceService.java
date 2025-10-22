package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.repository.BookRepository;
import com.columbia.coms4156.citationservice.repository.VideoRepository;
import com.columbia.coms4156.citationservice.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for source-related business logic.
 * Handles CRUD operations for various citation types and citation formatting.
 * Currently supports books, videos, and articles.
 */
@Service
public class SourceService {

  /**
   * Repository for managing Book entities.
   */
  @Autowired
  private BookRepository bookRepository;

  /**
   * Repository for managing Video entities.
   */
  @Autowired
  private VideoRepository videoRepository;

  /**
   * Repository for managing Article entities.
   */
  @Autowired
  private ArticleRepository articleRepository;

  // --- Book Methods ---
  /**
   * Saves a Book entity to the database.
   *
   * @param book the Book entity to save
   * @return the saved Book entity
   */
  public Book saveBook(Book book) {
    return bookRepository.save(book);
  }

  /**
   * Finds a Book entity by its ID.
   *
   * @param id the ID of the Book to find
   * @return an Optional containing the Book if found, empty otherwise
   */
  public Optional<Book> findBookById(Long id) {
    return bookRepository.findById(id);
  }

  /**
   * Retrieves all Book entities from the database.
   *
   * @return a list of all Book entities
   */
  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  /**
   * Deletes a Book entity by its ID.
   *
   * @param id the ID of the Book to delete
   */
  public void deleteBook(Long id) {
    bookRepository.deleteById(id);
  }

  /**
   * Updates an existing Book entity with new data.
   *
   * @param id the ID of the Book to update
   * @param updatedBook the Book entity containing updated data
   * @return the updated Book entity, or null if not found
   */
  public Book updateBook(Long id, Book updatedBook) {
    return bookRepository.findById(id)
            .map(book -> {
              book.setTitle(updatedBook.getTitle());
              book.setAuthor(updatedBook.getAuthor());
              book.setPublisher(updatedBook.getPublisher());
              book.setPublicationYear(updatedBook.getPublicationYear());
              book.setCity(updatedBook.getCity());
              book.setEdition(updatedBook.getEdition());
              book.setIsbn(updatedBook.getIsbn());
              return bookRepository.save(book);
            })
            .orElse(null);
  }

  // --- Video Methods ---
  /**
   * Saves a Video entity to the database.
   *
   * @param video the Video entity to save
   * @return the saved Video entity
   */
  public Video saveVideo(Video video) {
    return videoRepository.save(video);
  }

  /**
   * Finds a Video entity by its ID.
   *
   * @param id the ID of the Video to find
   * @return an Optional containing the Video if found, empty otherwise
   */
  public Optional<Video> findVideoById(Long id) {
    return videoRepository.findById(id);
  }

  /**
   * Retrieves all Video entities from the database.
   *
   * @return a list of all Video entities
   */
  public List<Video> getAllVideos() {
    return videoRepository.findAll();
  }

  /**
   * Deletes a Video entity by its ID.
   *
   * @param id the ID of the Video to delete
   */
  public void deleteVideo(Long id) {
    videoRepository.deleteById(id);
  }

  /**
   * Updates an existing Video entity with new data.
   *
   * @param id the ID of the Video to update
   * @param updatedVideo the Video entity containing updated data
   * @return the updated Video entity, or null if not found
   */
  public Video updateVideo(Long id, Video updatedVideo) {
    return videoRepository.findById(id)
            .map(video -> {
              video.setTitle(updatedVideo.getTitle());
              video.setAuthor(updatedVideo.getAuthor());
              video.setDirector(updatedVideo.getDirector());
              video.setDurationSeconds(updatedVideo.getDurationSeconds());
              video.setPlatform(updatedVideo.getPlatform());
              video.setUrl(updatedVideo.getUrl());
              video.setReleaseYear(updatedVideo.getReleaseYear());
              return videoRepository.save(video);
            })
            .orElse(null);
  }

  // --- Article Methods ---
  /**
   * Saves an Article entity to the database.
   *
   * @param article the Article entity to save
   * @return the saved Article entity
   */
  public Article saveArticle(Article article) {
    return articleRepository.save(article);
  }

  /**
   * Finds an Article entity by its ID.
   *
   * @param id the ID of the Article to find
   * @return an Optional containing the Article if found, empty otherwise
   */
  public Optional<Article> findArticleById(Long id) {
    return articleRepository.findById(id);
  }

  /**
   * Retrieves all Article entities from the database.
   *
   * @return a list of all Article entities
   */
  public List<Article> getAllArticles() {
    return articleRepository.findAll();
  }

  /**
   * Deletes an Article entity by its ID.
   *
   * @param id the ID of the Article to delete
   */
  public void deleteArticle(Long id) {
    articleRepository.deleteById(id);
  }

  /**
   * Updates an existing Article entity with new data.
   *
   * @param id the ID of the Article to update
   * @param updatedArticle the Article entity containing updated data
   * @return the updated Article entity, or null if not found
   */
  public Article updateArticle(Long id, Article updatedArticle) {
    return articleRepository.findById(id)
            .map(article -> {
              article.setTitle(updatedArticle.getTitle());
              article.setAuthor(updatedArticle.getAuthor());
              article.setJournal(updatedArticle.getJournal());
              article.setVolume(updatedArticle.getVolume());
              article.setIssue(updatedArticle.getIssue());
              article.setPages(updatedArticle.getPages());
              article.setDoi(updatedArticle.getDoi());
              article.setUrl(updatedArticle.getUrl());
              article.setPublicationYear(updatedArticle.getPublicationYear());
              return articleRepository.save(article);
            })
            .orElse(null);
  }
}
