package com.columbia.coms4156.citationservice.controller;

import com.columbia.coms4156.citationservice.controller.dto.BulkSourceRequest;
import com.columbia.coms4156.citationservice.controller.dto.SourceBatchResponse;
import com.columbia.coms4156.citationservice.exception.ResourceNotFoundException;
import com.columbia.coms4156.citationservice.exception.ValidationException;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.service.SourceService;
import com.columbia.coms4156.citationservice.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

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

  /** Logger for this class. */
  private static final Logger LOGGER = LoggerFactory.getLogger(SourceController.class);

  /**
   * Service for source management operations.
   */
  @Autowired
  private SourceService sourceService;

  /**
   * Validates that an ID is not null and is positive.
   *
   * @param id the ID to validate
   * @param resourceName the name of the resource (e.g., "Book", "Video")
   * @throws ValidationException if the ID is invalid
   */
  private void validateId(Long id, String resourceName) {
    if (id == null || id <= 0) {
      throw new ValidationException(
          resourceName + " ID must be a positive integer. Provided ID: " + id);
    }
  }

  // Book Endpoints
  /**
   * Create a new book citation source in the database.
   *
   * @param book The book entity to create
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the created book with HTTP 201 status if successful,
   *         or HTTP 500 if an error occurs
   */
  @PostMapping("/book")
  public ResponseEntity<?> createBook(@Valid @RequestBody Book book,
                                       HttpServletRequest request) {
    LOGGER.info("Received request to create book: {}", book);
    if (book == null) {
      throw new ValidationException(
          "Request body cannot be null. Please provide book information.");
    }

    Book savedBook = sourceService.saveBook(book);
    LOGGER.info("Successfully created book with ID: {}", savedBook.getId());
    return ResponseUtil.created(savedBook);
  }

  /**
   * Retrieve all book citation sources from the database.
   *
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing a list of all books with HTTP 200 status,
   *         or HTTP 500 if an error occurs
   */
  @GetMapping("/book")
  public ResponseEntity<?> getAllBooks(HttpServletRequest request) {
    LOGGER.info("Received request to retrieve all books");
    List<Book> books = sourceService.getAllBooks();
    LOGGER.info("Successfully retrieved {} books", books.size());
    return ResponseUtil.ok(books);
  }

  /**
   * Retrieve a specific book citation source by its unique identifier.
   *
   * @param id The unique identifier of the book to retrieve
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the book with HTTP 200 status if found,
   *         or HTTP 404 if the book doesn't exist
   */
  @GetMapping("/book/{id}")
  public ResponseEntity<?> getBookById(@PathVariable Long id, HttpServletRequest request) {
    LOGGER.info("Received request to retrieve book with ID: {}", id);
    validateId(id, "Book");

    Book book = sourceService.findBookById(id)
        .orElseThrow(() -> {
          LOGGER.warn("Book not found for ID: {}", id);
          return new ResourceNotFoundException(
              "No book found with ID: " + id + ". Please verify the book ID and try again.");
        });
    LOGGER.info("Successfully retrieved book: {}", book);
    return ResponseUtil.ok(book);
  }

  /**
   * Update an existing book citation source with new information.
   *
   * @param id The unique identifier of the book to update
   * @param book The book entity containing updated information
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the updated book with HTTP 200 status if successful,
   *         HTTP 404 if the book doesn't exist, or HTTP 500 if an error occurs
   */
  @PutMapping("/book/{id}")
  public ResponseEntity<?> updateBook(@PathVariable Long id,
                                        @Valid @RequestBody Book book,
                                        HttpServletRequest request) {
    validateId(id, "Book");
    if (book == null) {
      throw new ValidationException(
          "Request body cannot be null. Please provide book information.");
    }

    Book updatedBook = sourceService.updateBook(id, book);
    if (updatedBook == null) {
      LOGGER.warn("Book not found for update with ID: {}", id);
      throw new ResourceNotFoundException(
          "No book found with ID: " + id + ". Cannot update non-existent book.");
    }
    LOGGER.info("Successfully updated book with ID: {}", id);
    return ResponseUtil.ok(updatedBook);
  }

  /**
   * Delete a book citation source from the database.
   *
   * @param id The unique identifier of the book to delete
   * @param request The HTTP request object for error context
   * @return ResponseEntity with HTTP 204 status if successfully deleted,
   *         HTTP 404 if the book doesn't exist, or HTTP 500 if an error occurs
   */
  @DeleteMapping("/book/{id}")
  public ResponseEntity<?> deleteBook(@PathVariable Long id, HttpServletRequest request) {
    LOGGER.info("Received request to delete book with ID: {}", id);
    validateId(id, "Book");

    sourceService.findBookById(id)
        .orElseThrow(() -> {
          LOGGER.warn("Book not found for delete with ID: {}", id);
          return new ResourceNotFoundException(
              "No book found with ID: " + id + ". Cannot delete non-existent book.");
        });
    sourceService.deleteBook(id);
    LOGGER.info("Successfully deleted book with ID: {}", id);
    return ResponseUtil.noContent();
  }

  // --- Video Endpoints ---
  /**
   * Create a new video citation source in the database.
   *
   * @param video The video entity to create
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the created video with HTTP 201 status if successful,
   *         or HTTP 500 if an error occurs
   */
  @PostMapping("/video")
  public ResponseEntity<?> createVideo(@Valid @RequestBody Video video,
                                        HttpServletRequest request) {
    LOGGER.info("Received request to create video: {}", video);
    if (video == null) {
      throw new ValidationException(
          "Request body cannot be null. Please provide video information.");
    }

    Video savedVideo = sourceService.saveVideo(video);
    LOGGER.info("Successfully created video with ID: {}", savedVideo.getId());
    return ResponseUtil.created(savedVideo);
  }

  /**
   * Retrieve all video citation sources from the database.
   *
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing a list of all videos with HTTP 200 status,
   *         or HTTP 500 if an error occurs
   */
  @GetMapping("/video")
  public ResponseEntity<?> getAllVideos(HttpServletRequest request) {
    LOGGER.info("Received request to retrieve all videos");
    List<Video> videos = sourceService.getAllVideos();
    LOGGER.info("Successfully retrieved {} videos", videos.size());
    return ResponseUtil.ok(videos);
  }

  /**
   * Retrieve a specific video citation source by its unique identifier.
   *
   * @param id The unique identifier of the video to retrieve
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the video with HTTP 200 status if found,
   *         or HTTP 404 if the video doesn't exist
   */
  @GetMapping("/video/{id}")
  public ResponseEntity<?> getVideoById(@PathVariable Long id, HttpServletRequest request) {
    LOGGER.info("Received request to retrieve video with ID: {}", id);
    validateId(id, "Video");

    Video video = sourceService.findVideoById(id)
        .orElseThrow(() -> {
          LOGGER.warn("Video not found for ID: {}", id);
          return new ResourceNotFoundException(
              "No video found with ID: " + id + ". Please verify the video ID and try again.");
        });
    LOGGER.info("Successfully retrieved video: {}", video);
    return ResponseUtil.ok(video);
  }

  /**
   * Update an existing video citation source with new information.
   *
   * @param id The unique identifier of the video to update
   * @param video The video entity containing updated information
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the updated video with HTTP 200 status if successful,
   *         HTTP 404 if the video doesn't exist, or HTTP 500 if an error occurs
   */
  @PutMapping("/video/{id}")
  public ResponseEntity<?> updateVideo(@PathVariable Long id,
                                      @Valid @RequestBody Video video,
                                      HttpServletRequest request) {
    LOGGER.info("Received request to update video with ID: {}", id);
    validateId(id, "Video");
    if (video == null) {
      throw new ValidationException(
          "Request body cannot be null. Please provide video information.");
    }

    Video updatedVideo = sourceService.updateVideo(id, video);
    if (updatedVideo == null) {
      LOGGER.warn("Video not found for update with ID: {}", id);
      throw new ResourceNotFoundException(
          "No video found with ID: " + id + ". Cannot update non-existent video.");
    }
    LOGGER.info("Successfully updated video with ID: {}", id);
    return ResponseUtil.ok(updatedVideo);
  }

  /**
   * Delete a video citation source from the database.
   *
   * @param id The unique identifier of the video to delete
   * @param request The HTTP request object for error context
   * @return ResponseEntity with HTTP 204 status if successfully deleted,
   *         HTTP 404 if the video doesn't exist, or HTTP 500 if an error occurs
   */
  @DeleteMapping("/video/{id}")
  public ResponseEntity<?> deleteVideo(@PathVariable Long id, HttpServletRequest request) {
    LOGGER.info("Received request to delete video with ID: {}", id);
    validateId(id, "Video");

    sourceService.findVideoById(id)
        .orElseThrow(() -> {
          LOGGER.warn("Video not found for delete with ID: {}", id);
          return new ResourceNotFoundException(
              "No video found with ID: " + id + ". Cannot delete non-existent video.");
        });
    sourceService.deleteVideo(id);
    LOGGER.info("Successfully deleted video with ID: {}", id);
    return ResponseUtil.noContent();
  }

  // --- Article Endpoints ---
  /**
   * Create a new article citation source in the database.
   *
   * @param article The article entity to create
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the created article with HTTP 201 status if successful,
   *         or HTTP 500 if an error occurs
   */
  @PostMapping("/article")
  public ResponseEntity<?> createArticle(@Valid @RequestBody Article article,
                                         HttpServletRequest request) {
    LOGGER.info("Received request to create article: {}", article);
    if (article == null) {
      throw new ValidationException(
          "Request body cannot be null. Please provide article information.");
    }

    Article savedArticle = sourceService.saveArticle(article);
    LOGGER.info("Successfully created article with ID: {}", savedArticle.getId());
    return ResponseUtil.created(savedArticle);
  }

  /**
   * Retrieve all article citation sources from the database.
   *
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing a list of all articles with HTTP 200 status,
   *         or HTTP 500 if an error occurs
   */
  @GetMapping("/article")
  public ResponseEntity<?> getAllArticles(HttpServletRequest request) {
    LOGGER.info("Received request to retrieve all articles");
    List<Article> articles = sourceService.getAllArticles();
    LOGGER.info("Successfully retrieved {} articles", articles.size());
    return ResponseUtil.ok(articles);
  }

  /**
   * Retrieve a specific article citation source by its unique identifier.
   *
   * @param id The unique identifier of the article to retrieve
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the article with HTTP 200 status if found,
   *         or HTTP 404 if the article doesn't exist
   */
  @GetMapping("/article/{id}")
  public ResponseEntity<?> getArticleById(@PathVariable Long id, HttpServletRequest request) {
    LOGGER.info("Received request to retrieve article with ID: {}", id);
    validateId(id, "Article");

    Article article = sourceService.findArticleById(id)
        .orElseThrow(() -> {
          LOGGER.warn("Article not found for ID: {}", id);
          return new ResourceNotFoundException(
              "No article found with ID: " + id + ". Please verify the article ID and try again.");
        });
    LOGGER.info("Successfully retrieved article: {}", article);
    return ResponseUtil.ok(article);
  }

  /**
   * Update an existing article citation source with new information.
   *
   * @param id The unique identifier of the article to update
   * @param article The article entity containing updated information
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the updated article with HTTP 200 status if successful,
   *         HTTP 404 if the article doesn't exist, or HTTP 500 if an error occurs
   */
  @PutMapping("/article/{id}")
  public ResponseEntity<?> updateArticle(@PathVariable Long id,
                                        @Valid @RequestBody Article article,
                                        HttpServletRequest request) {
    LOGGER.info("Received request to update article with ID: {}", id);
    validateId(id, "Article");
    if (article == null) {
      throw new ValidationException(
          "Request body cannot be null. Please provide article information.");
    }

    Article updatedArticle = sourceService.updateArticle(id, article);
    if (updatedArticle == null) {
      LOGGER.warn("Article not found for update with ID: {}", id);
      throw new ResourceNotFoundException(
          "No article found with ID: " + id + ". Cannot update non-existent article.");
    }
    LOGGER.info("Successfully updated article with ID: {}", id);
    return ResponseUtil.ok(updatedArticle);
  }

  /**
   * Delete an article citation source from the database.
   *
   * @param id The unique identifier of the article to delete
   * @param request The HTTP request object for error context
   * @return ResponseEntity with HTTP 204 status if successfully deleted,
   *         HTTP 404 if the article doesn't exist, or HTTP 500 if an error occurs
   */
  @DeleteMapping("/article/{id}")
  public ResponseEntity<?> deleteArticle(@PathVariable Long id, HttpServletRequest request) {
    LOGGER.info("Received request to delete article with ID: {}", id);
    validateId(id, "Article");

    sourceService.findArticleById(id)
        .orElseThrow(() -> {
          LOGGER.warn("Article not found for delete with ID: {}", id);
          return new ResourceNotFoundException(
              "No article found with ID: " + id + ". Cannot delete non-existent article.");
        });
    sourceService.deleteArticle(id);
    LOGGER.info("Successfully deleted article with ID: {}", id);
    return ResponseUtil.noContent();
  }

  // Batch endpoint: accepts multiple sources and optional submissionId
  /**
   * Add or append multiple sources to the database.
   *
   * @param request The bulk source request containing multiple sources to add
   * @param submissionId Optional submission ID for tracking
   * @return ResponseEntity containing the batch response with HTTP 200 status if successful,
   *         or HTTP 500 if an error occurs. The batch response will contain the
   *         submissionId for group source tracking,
   *         citationIds for each source, which can be used to cite sources individually,
   *         errors for any errors that occur.
   *
   */
  @PostMapping("/sources")
  public ResponseEntity<SourceBatchResponse> addSources(
      @RequestBody BulkSourceRequest request,
      @RequestParam(value = "submissionId", required = false) Long submissionId) {
    LOGGER.info("Received request to add bulk sources. SubmissionId: {}", submissionId);
    // return 400 Bad Request when the request has no sources
    if (request == null || request.getSources() == null || request.getSources().isEmpty()) {
      LOGGER.warn("Received empty bulk source request");
      SourceBatchResponse resp = new SourceBatchResponse();
      resp.setSubmissionId(submissionId);
      resp.setCitationIds(new ArrayList<>());
      resp.setErrors(Arrays.asList("No sources provided in request"));
      return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
    SourceBatchResponse resp = sourceService.addOrAppendSources(request, submissionId);
    LOGGER.info("Successfully processed bulk sources. SubmissionId: {}", resp.getSubmissionId());
    return ResponseUtil.ok(resp);
  }
}