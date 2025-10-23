package com.columbia.coms4156.citationservice.controller;

import com.columbia.coms4156.citationservice.controller.dto.BulkSourceRequest;
import com.columbia.coms4156.citationservice.controller.dto.SourceBatchResponse;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Source management API endpoints.
 * Provides endpoints for CRUD operations stored source objects.
 * Currently supports the following Sources: books, videos, and articles.
 *
 * NOTE: These are not representative of the program's main purpose. Please follow
 * the implementation details on the project proposal.
 */
@RestController
@RequestMapping("/api/source")
@CrossOrigin(origins = "*")
public class SourceController {

  /**
   * Service for source management operations.
   */
  @Autowired
  private SourceService sourceService;

  // Book Endpoints
  /**
   * Create a new book citation source in the database.
   *
   * @param book The book entity to create
   * @return ResponseEntity containing the created book with HTTP 201 status if successful,
   *         or HTTP 500 if an error occurs
   */
  @PostMapping("/book")
  public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
    try {
      Book savedBook = sourceService.saveBook(book);
      return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Retrieve all book citation sources from the database.
   *
   * @return ResponseEntity containing a list of all books with HTTP 200 status,
   *         or HTTP 500 if an error occurs
   */
  @GetMapping("/book")
  public ResponseEntity<List<Book>> getAllBooks() {
    try {
      List<Book> books = sourceService.getAllBooks();
      return new ResponseEntity<>(books, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Retrieve a specific book citation source by its unique identifier.
   *
   * @param id The unique identifier of the book to retrieve
   * @return ResponseEntity containing the book with HTTP 200 status if found,
   *         or HTTP 404 if the book doesn't exist
   */
  @GetMapping("/book/{id}")
  public ResponseEntity<Book> getBookById(@PathVariable Long id) {
    Optional<Book> book = sourceService.findBookById(id);
    return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * Update an existing book citation source with new information.
   *
   * @param id The unique identifier of the book to update
   * @param book The book entity containing updated information
   * @return ResponseEntity containing the updated book with HTTP 200 status if successful,
   *         HTTP 404 if the book doesn't exist, or HTTP 500 if an error occurs
   */
  @PutMapping("/book/{id}")
  public ResponseEntity<Book> updateBook(@PathVariable Long id,
                                        @Valid @RequestBody Book book) {
    try {
      Book updatedBook = sourceService.updateBook(id, book);
      if (updatedBook != null) {
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Delete a book citation source from the database.
   *
   * @param id The unique identifier of the book to delete
   * @return ResponseEntity with HTTP 204 status if successfully deleted,
   *         HTTP 404 if the book doesn't exist, or HTTP 500 if an error occurs
   */
  @DeleteMapping("/book/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    try {
      Optional<Book> book = sourceService.findBookById(id);
      if (book.isPresent()) {
        sourceService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // --- Video Endpoints ---
  /**
   * Create a new video citation source in the database.
   *
   * @param video The video entity to create
   * @return ResponseEntity containing the created video with HTTP 201 status if successful,
   *         or HTTP 500 if an error occurs
   */
  @PostMapping("/video")
  public ResponseEntity<Video> createVideo(@Valid @RequestBody Video video) {
    try {
      Video saved = sourceService.saveVideo(video);
      return new ResponseEntity<>(saved, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Retrieve all video citation sources from the database.
   *
   * @return ResponseEntity containing a list of all videos with HTTP 200 status,
   *         or HTTP 500 if an error occurs
   */
  @GetMapping("/video")
  public ResponseEntity<List<Video>> getAllVideos() {
    try {
      List<Video> list = sourceService.getAllVideos();
      return new ResponseEntity<>(list, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Retrieve a specific video citation source by its unique identifier.
   *
   * @param id The unique identifier of the video to retrieve
   * @return ResponseEntity containing the video with HTTP 200 status if found,
   *         or HTTP 404 if the video doesn't exist
   */
  @GetMapping("/video/{id}")
  public ResponseEntity<Video> getVideoById(@PathVariable Long id) {
    Optional<Video> v = sourceService.findVideoById(id);
    return v.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * Update an existing video citation source with new information.
   *
   * @param id The unique identifier of the video to update
   * @param video The video entity containing updated information
   * @return ResponseEntity containing the updated video with HTTP 200 status if successful,
   *         HTTP 404 if the video doesn't exist, or HTTP 500 if an error occurs
   */
  @PutMapping("/video/{id}")
  public ResponseEntity<Video> updateVideo(@PathVariable Long id,
                                          @Valid @RequestBody Video video) {
    try {
      Video updated = sourceService.updateVideo(id, video);
      if (updated != null) {
        return new ResponseEntity<>(updated, HttpStatus.OK);
      }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Delete a video citation source from the database.
   *
   * @param id The unique identifier of the video to delete
   * @return ResponseEntity with HTTP 204 status if successfully deleted,
   *         HTTP 404 if the video doesn't exist, or HTTP 500 if an error occurs
   */
  @DeleteMapping("/video/{id}")
  public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
    try {
      Optional<Video> v = sourceService.findVideoById(id);
      if (v.isPresent()) {
        sourceService.deleteVideo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // --- Article Endpoints ---
  /**
   * Create a new article citation source in the database.
   *
   * @param article The article entity to create
   * @return ResponseEntity containing the created article with HTTP 201 status if successful,
   *         or HTTP 500 if an error occurs
   */
  @PostMapping("/article")
  public ResponseEntity<Article> createArticle(@Valid @RequestBody Article article) {
    try {
      Article saved = sourceService.saveArticle(article);
      return new ResponseEntity<>(saved, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Retrieve all article citation sources from the database.
   *
   * @return ResponseEntity containing a list of all articles with HTTP 200 status,
   *         or HTTP 500 if an error occurs
   */
  @GetMapping("/article")
  public ResponseEntity<List<Article>> getAllArticles() {
    try {
      List<Article> list = sourceService.getAllArticles();
      return new ResponseEntity<>(list, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Retrieve a specific article citation source by its unique identifier.
   *
   * @param id The unique identifier of the article to retrieve
   * @return ResponseEntity containing the article with HTTP 200 status if found,
   *         or HTTP 404 if the article doesn't exist
   */
  @GetMapping("/article/{id}")
  public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
    Optional<Article> a = sourceService.findArticleById(id);
    return a.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * Update an existing article citation source with new information.
   *
   * @param id The unique identifier of the article to update
   * @param article The article entity containing updated information
   * @return ResponseEntity containing the updated article with HTTP 200 status if successful,
   *         HTTP 404 if the article doesn't exist, or HTTP 500 if an error occurs
   */
  @PutMapping("/article/{id}")
  public ResponseEntity<Article> updateArticle(@PathVariable Long id,
                                              @Valid @RequestBody Article article) {
    try {
      Article updated = sourceService.updateArticle(id, article);
      if (updated != null) {
        return new ResponseEntity<>(updated, HttpStatus.OK);
      }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Delete an article citation source from the database.
   *
   * @param id The unique identifier of the article to delete
   * @return ResponseEntity with HTTP 204 status if successfully deleted,
   *         HTTP 404 if the article doesn't exist, or HTTP 500 if an error occurs
   */
  @DeleteMapping("/article/{id}")
  public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
    try {
      Optional<Article> a = sourceService.findArticleById(id);
      if (a.isPresent()) {
        sourceService.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Batch endpoint: accepts multiple sources and optional submissionId
  /**
   * Add or append multiple sources to the database.
   *
   * @param request The bulk source request containing multiple sources to add
   * @param submissionId Optional submission ID for tracking
   * @return ResponseEntity containing the batch response with HTTP 200 status if successful,
   *         or HTTP 500 if an error occurs
   */
  @PostMapping("/sources")
  public ResponseEntity<SourceBatchResponse> addSources(
      @RequestBody BulkSourceRequest request,
      @RequestParam(value = "submissionId", required = false) Long submissionId) {
    try {
      SourceBatchResponse resp = sourceService.addOrAppendSources(request, submissionId);
      return new ResponseEntity<>(resp, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}