package com.columbia.coms4156.citationservice.controller;

import com.columbia.coms4156.citationservice.controller.dto.BulkSourceRequest;
import com.columbia.coms4156.citationservice.controller.dto.SourceBatchResponse;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.model.ErrorResponse;
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
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
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
   * @param backfill Whether to attempt to backfill missing data from Google Books API
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the created book with HTTP 201 status if successful,
   *         or HTTP 500 if an error occurs
   */
  @PostMapping("/book")
  public ResponseEntity<?> createBook(@Valid @RequestBody Book book,
                                      @RequestParam(defaultValue = "false") boolean backfill,
                                      HttpServletRequest request) {
    try {
      if (book == null) {
        ErrorResponse error = new ErrorResponse(
            "Missing Book Data",
            "Request body cannot be null. Please provide book information.",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      Book savedBook = sourceService.saveBook(book, backfill);
      return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Book Creation Error",
          "An unexpected error occurred while creating the book: " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      List<Book> books = sourceService.getAllBooks();
      return new ResponseEntity<>(books, HttpStatus.OK);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Database Error",
          "An unexpected error occurred while retrieving books: " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      if (id == null || id <= 0) {
        ErrorResponse error = new ErrorResponse(
            "Invalid ID",
            "Book ID must be a positive integer. Provided ID: " + id,
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      Optional<Book> book = sourceService.findBookById(id);
      if (book.isPresent()) {
        return new ResponseEntity<>(book.get(), HttpStatus.OK);
      } else {
        ErrorResponse error = new ErrorResponse(
            "Book Not Found",
            "No book found with ID: " + id + ". Please verify the book ID and try again.",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Database Error",
          "An unexpected error occurred while retrieving book with ID " + id + ": "
              + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      if (id == null || id <= 0) {
        ErrorResponse error = new ErrorResponse(
            "Invalid ID",
            "Book ID must be a positive integer. Provided ID: " + id,
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      if (book == null) {
        ErrorResponse error = new ErrorResponse(
            "Missing Book Data",
            "Request body cannot be null. Please provide book information.",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      Book updatedBook = sourceService.updateBook(id, book);
      if (updatedBook != null) {
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
      } else {
        ErrorResponse error = new ErrorResponse(
            "Book Not Found",
            "No book found with ID: " + id + ". Cannot update non-existent book.",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Book Update Error",
          "An unexpected error occurred while updating book with ID " + id + ": "
              + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      if (id == null || id <= 0) {
        ErrorResponse error = new ErrorResponse(
            "Invalid ID",
            "Book ID must be a positive integer. Provided ID: " + id,
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      Optional<Book> book = sourceService.findBookById(id);
      if (book.isPresent()) {
        sourceService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } else {
        ErrorResponse error = new ErrorResponse(
            "Book Not Found",
            "No book found with ID: " + id + ". Cannot delete non-existent book.",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Book Deletion Error",
          "An unexpected error occurred while deleting book with ID " + id + ": "
              + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      if (video == null) {
        ErrorResponse error = new ErrorResponse(
            "Missing Video Data",
            "Request body cannot be null. Please provide video information.",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      Video savedVideo = sourceService.saveVideo(video);
      return new ResponseEntity<>(savedVideo, HttpStatus.CREATED);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Video Creation Error",
          "An unexpected error occurred while creating the video: " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      List<Video> videos = sourceService.getAllVideos();
      return new ResponseEntity<>(videos, HttpStatus.OK);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Database Error",
          "An unexpected error occurred while retrieving videos: " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      if (id == null || id <= 0) {
        ErrorResponse error = new ErrorResponse(
            "Invalid ID",
            "Video ID must be a positive integer. Provided ID: " + id,
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      Optional<Video> video = sourceService.findVideoById(id);
      if (video.isPresent()) {
        return new ResponseEntity<>(video.get(), HttpStatus.OK);
      } else {
        ErrorResponse error = new ErrorResponse(
            "Video Not Found",
            "No video found with ID: " + id + ". Please verify the video ID and try again.",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Database Error",
          "An unexpected error occurred while retrieving video with ID " + id + ": "
              + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      if (id == null || id <= 0) {
        ErrorResponse error = new ErrorResponse(
            "Invalid ID",
            "Video ID must be a positive integer. Provided ID: " + id,
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      if (video == null) {
        ErrorResponse error = new ErrorResponse(
            "Missing Video Data",
            "Request body cannot be null. Please provide video information.",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      Video updatedVideo = sourceService.updateVideo(id, video);
      if (updatedVideo != null) {
        return new ResponseEntity<>(updatedVideo, HttpStatus.OK);
      } else {
        ErrorResponse error = new ErrorResponse(
            "Video Not Found",
            "No video found with ID: " + id + ". Cannot update non-existent video.",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Video Update Error",
          "An unexpected error occurred while updating video with ID " + id + ": "
              + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      if (id == null || id <= 0) {
        ErrorResponse error = new ErrorResponse(
            "Invalid ID",
            "Video ID must be a positive integer. Provided ID: " + id,
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      Optional<Video> video = sourceService.findVideoById(id);
      if (video.isPresent()) {
        sourceService.deleteVideo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } else {
        ErrorResponse error = new ErrorResponse(
            "Video Not Found",
            "No video found with ID: " + id + ". Cannot delete non-existent video.",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Video Deletion Error",
          "An unexpected error occurred while deleting video with ID " + id + ": "
              + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      if (article == null) {
        ErrorResponse error = new ErrorResponse(
            "Missing Article Data",
            "Request body cannot be null. Please provide article information.",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      Article savedArticle = sourceService.saveArticle(article);
      return new ResponseEntity<>(savedArticle, HttpStatus.CREATED);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Article Creation Error",
          "An unexpected error occurred while creating the article: " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      List<Article> articles = sourceService.getAllArticles();
      return new ResponseEntity<>(articles, HttpStatus.OK);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Database Error",
          "An unexpected error occurred while retrieving articles: " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      if (id == null || id <= 0) {
        ErrorResponse error = new ErrorResponse(
            "Invalid ID",
            "Article ID must be a positive integer. Provided ID: " + id,
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      Optional<Article> article = sourceService.findArticleById(id);
      if (article.isPresent()) {
        return new ResponseEntity<>(article.get(), HttpStatus.OK);
      } else {
        ErrorResponse error = new ErrorResponse(
            "Article Not Found",
            "No article found with ID: " + id + ". Please verify the article ID and try again.",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Database Error",
          "An unexpected error occurred while retrieving article with ID " + id + ": "
              + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      if (id == null || id <= 0) {
        ErrorResponse error = new ErrorResponse(
            "Invalid ID",
            "Article ID must be a positive integer. Provided ID: " + id,
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      if (article == null) {
        ErrorResponse error = new ErrorResponse(
            "Missing Article Data",
            "Request body cannot be null. Please provide article information.",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      Article updatedArticle = sourceService.updateArticle(id, article);
      if (updatedArticle != null) {
        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
      } else {
        ErrorResponse error = new ErrorResponse(
            "Article Not Found",
            "No article found with ID: " + id + ". Cannot update non-existent article.",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Article Update Error",
          "An unexpected error occurred while updating article with ID " + id + ": "
              + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
    try {
      if (id == null || id <= 0) {
        ErrorResponse error = new ErrorResponse(
            "Invalid ID",
            "Article ID must be a positive integer. Provided ID: " + id,
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      Optional<Article> article = sourceService.findArticleById(id);
      if (article.isPresent()) {
        sourceService.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } else {
        ErrorResponse error = new ErrorResponse(
            "Article Not Found",
            "No article found with ID: " + id + ". Cannot delete non-existent article.",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Article Deletion Error",
          "An unexpected error occurred while deleting article with ID " + id + ": "
              + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Batch endpoint: accepts multiple sources and optional submissionId
  /**
   * Add or append multiple sources to the database.
   *
   * @param request The bulk source request containing multiple sources to add
   * @param submissionId Optional submission ID for tracking
   * @param backfill Whether to attempt to backfill missing data
   *                 from Google Books API for book sources
   * @return ResponseEntity containing the batch response with HTTP 200 status if successful,
   *         or HTTP 500 if an error occurs
   */
  @PostMapping("/sources")
  public ResponseEntity<SourceBatchResponse> addSources(
      @RequestBody BulkSourceRequest request,
      @RequestParam(value = "submissionId", required = false) Long submissionId,
      @RequestParam(defaultValue = "false") boolean backfill) {
    try {
      // return 400 Bad Request when the request has no sources
      if (request == null || request.getSources() == null || request.getSources().isEmpty()) {
        SourceBatchResponse resp = new SourceBatchResponse();
        resp.setSubmissionId(submissionId);
        resp.setSourceIds(new ArrayList<>());
        resp.setErrors(Arrays.asList("No sources provided in request"));
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
      }
      SourceBatchResponse resp = sourceService.addOrAppendSources(request, submissionId, backfill);
      return new ResponseEntity<>(resp, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      SourceBatchResponse resp = new SourceBatchResponse();
      resp.setSubmissionId(submissionId);
      resp.setSourceIds(new ArrayList<>());
      resp.setErrors(Arrays.asList(e.getMessage() == null ? "Resource not found" : e.getMessage()));
      return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      SourceBatchResponse resp = new SourceBatchResponse();
      resp.setSubmissionId(submissionId);
      resp.setSourceIds(new ArrayList<>());
      resp.setErrors(Arrays.asList("Internal server error: " + (e.getMessage() == null
              ? "unexpected error" : e.getMessage())));
      return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}