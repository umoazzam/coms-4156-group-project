package com.columbia.coms4156.citationservice.controller;

import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.CitationResponse;
import com.columbia.coms4156.citationservice.model.ErrorResponse;
import com.columbia.coms4156.citationservice.model.GroupCitationResponse;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.service.CitationService;
import com.columbia.coms4156.citationservice.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * REST Controller for Citation creation API endpoints.
 * Provides endpoints for citation generation for various source types and styles.
 * Currently supports MLA format for books, videos, and articles.
 */
@RestController
@RequestMapping("/api/cite")
@CrossOrigin(origins = "*")
public class CitationController {

  /**
   * Service for citation generation operations.
   */
  @Autowired
  private CitationService citationService;

  /**
   * Service for source management operations.
   */
  @Autowired
  private SourceService sourceService;

  // --- Book Endpoints ---
  /**
   * Generate an MLA format citation for a stored book.
   *
   * @param id The unique identifier of the book to generate citation for
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if book not found, or HTTP 500 if an error occurs
   */
  @GetMapping("/book/{id}")
  public ResponseEntity<?> generateBookCitation(@PathVariable Long id, HttpServletRequest request) {
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
        String citation = citationService.generateMLACitation(book.get());
        return new ResponseEntity<>(citation, HttpStatus.OK);
      } else {
        ErrorResponse error = new ErrorResponse(
            "Book Not Found",
            "No book found with ID: " + id + ". Please verify the book ID and try again.",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
      }
    } catch (IllegalArgumentException e) {
      ErrorResponse error = new ErrorResponse(
          "Invalid Book Data",
          "Unable to generate citation: " + e.getMessage(),
          HttpStatus.BAD_REQUEST.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Citation Generation Error",
          "An unexpected error occurred while generating the citation for book ID " + id
              + ": " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Generate an MLA format citation given a book.
   *
   * @param book The book object to generate a citation for. This is an Ad-Hoc creation and the
   *             book will not be saved.
   * @param style The citation style (MLA, APA, Chicago)
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if book not found, or HTTP 500 if an error occurs
   */
  @PostMapping("/book")
  public ResponseEntity<?> generateBookCitationFromData(
      @Valid @RequestBody Book book,
      @RequestParam(defaultValue = "MLA") String style,
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

      if (style == null || style.trim().isEmpty()) {
        ErrorResponse error = new ErrorResponse(
            "Invalid Style Parameter",
            "Citation style cannot be null or empty. Supported styles: MLA, APA, CHICAGO",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      String citation = citationService.generateCitationByStyle(book, style);
      return new ResponseEntity<>(citation, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      ErrorResponse error = new ErrorResponse(
          "Invalid Book Data",
          "Unable to generate citation: " + e.getMessage()
              + ". Please check that all required fields are provided.",
          HttpStatus.BAD_REQUEST.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Citation Generation Error",
          "An unexpected error occurred while generating the citation: "
              + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // --- Video Endpoints ---
  /**
   * Generate an MLA format citation for a stored video.
   *
   * @param id The unique identifier of the video to generate citation for
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if video not found, or HTTP 500 if an error occurs
   */
  @GetMapping("/video/{id}")
  public ResponseEntity<?> generateVideoCitation(@PathVariable Long id,
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

      Optional<Video> video = sourceService.findVideoById(id);
      if (video.isPresent()) {
        String citation = citationService.generateMLACitation(video.get());
        return new ResponseEntity<>(citation, HttpStatus.OK);
      } else {
        ErrorResponse error = new ErrorResponse(
            "Video Not Found",
            "No video found with ID: " + id + ". Please verify the video ID and try again.",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
      }
    } catch (IllegalArgumentException e) {
      ErrorResponse error = new ErrorResponse(
          "Invalid Video Data",
          "Unable to generate citation: " + e.getMessage(),
          HttpStatus.BAD_REQUEST.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Citation Generation Error",
          "An unexpected error occurred while generating the citation for video ID " + id
              + ": " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Generate an MLA format citation given a video.
   *
   * @param video The video object to generate a citation for. This is an Ad-Hoc creation and the
   *             video will not be saved.
   * @param style The citation style (MLA, APA, Chicago)
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if video not found, or HTTP 500 if an error occurs
   */
  @PostMapping("/video/citation")
  public ResponseEntity<?> generateVideoCitationFromData(
      @Valid @RequestBody Video video,
      @RequestParam(defaultValue = "MLA") String style,
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

      if (style == null || style.trim().isEmpty()) {
        ErrorResponse error = new ErrorResponse(
            "Invalid Style Parameter",
            "Citation style cannot be null or empty. Supported styles: MLA, APA, CHICAGO",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      String citation = citationService.generateCitationByStyle(video, style);
      return new ResponseEntity<>(citation, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      ErrorResponse error = new ErrorResponse(
          "Invalid Video Data",
          "Unable to generate citation: " + e.getMessage()
              + ". Please check that all required fields are provided.",
          HttpStatus.BAD_REQUEST.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Citation Generation Error",
          "An unexpected error occurred while generating the citation: "
              + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // --- Article Endpoints ---
  /**
   * Generate an MLA format citation for a stored article.
   *
   * @param id The unique identifier of the article to generate citation for
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if article is not found, or HTTP 500 if an error occurs
   */
  @GetMapping("/article/{id}/citation")
  public ResponseEntity<?> generateArticleCitation(@PathVariable Long id,
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

      Optional<Article> article = sourceService.findArticleById(id);
      if (article.isPresent()) {
        String citation = citationService.generateMLACitation(article.get());
        return new ResponseEntity<>(citation, HttpStatus.OK);
      } else {
        ErrorResponse error = new ErrorResponse(
            "Article Not Found",
            "No article found with ID: " + id + ". Please verify the article ID and try again.",
            HttpStatus.NOT_FOUND.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
      }
    } catch (IllegalArgumentException e) {
      ErrorResponse error = new ErrorResponse(
          "Invalid Article Data",
          "Unable to generate citation: " + e.getMessage(),
          HttpStatus.BAD_REQUEST.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Citation Generation Error",
          "An unexpected error occurred while generating the citation for article ID " + id
              + ": " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Generate an MLA format citation given an article.
   *
   * @param article The article object to generate a citation for. This is an Ad-Hoc creation
   *                and the article will not be saved.
   * @param style The citation style (MLA, APA, Chicago)
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if article not found, or HTTP 500 if an error occurs
   */
  @PostMapping("/article/citation")
  public ResponseEntity<?> generateArticleCitationFromData(
      @Valid @RequestBody Article article,
      @RequestParam(defaultValue = "MLA") String style,
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

      if (style == null || style.trim().isEmpty()) {
        ErrorResponse error = new ErrorResponse(
            "Invalid Style Parameter",
            "Citation style cannot be null or empty. Supported styles: MLA, APA, CHICAGO",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      String citation = citationService.generateCitationByStyle(article, style);
      return new ResponseEntity<>(citation, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      ErrorResponse error = new ErrorResponse(
          "Invalid Article Data",
          "Unable to generate citation: " + e.getMessage()
              + ". Please check that all required fields are provided.",
          HttpStatus.BAD_REQUEST.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Citation Generation Error",
          "An unexpected error occurred while generating the citation: "
              + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // --- General Use Endpoints ---
  /**
   * UPDATE NEEDED FOR NEW API FLOW
   * Generate citation for a single source by sourceId with specified style and backfill option.
   *
   * @param sourceId The unique identifier of the source to generate citation for
   * @param style The citation style (MLA, APA, Chicago)
   * @param backfill Whether to include backfill information (currently not implemented)
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing CitationResponse with HTTP 200 status if successful,
   * HTTP 404 with error message if source not found, or HTTP 400 if invalid parameters
   */
  @GetMapping("/source/{sourceId}")
  public ResponseEntity<?> generateCitationForSource(
      @PathVariable Long sourceId,
      @RequestParam(defaultValue = "MLA") String style,
      @RequestParam(defaultValue = "false") boolean backfill,
      HttpServletRequest request) {
    try {
      if (sourceId == null || sourceId <= 0) {
        ErrorResponse error = new ErrorResponse(
            "Invalid Source ID",
            "Source ID must be a positive integer. Provided ID: " + sourceId,
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      if (style == null || style.trim().isEmpty()) {
        ErrorResponse error = new ErrorResponse(
            "Invalid Style Parameter",
            "Citation style cannot be null or empty. Supported styles: MLA, APA, CHICAGO",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      CitationResponse response = citationService.generateCitationForSource(
          sourceId, style, backfill);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      ErrorResponse error = new ErrorResponse(
          "Source Not Found",
          "No source found with ID: " + sourceId + ". " + e.getMessage(),
          HttpStatus.NOT_FOUND.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Citation Generation Error",
          "An unexpected error occurred while generating citation for source ID " + sourceId
              + ": " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * UPDATE NEEDED FOR NEW API FLOW
   * Generate citations for all sources in a submission group.
   *
   * @param submissionId The unique identifier of the submission group
   * @param style The citation style (MLA, APA, Chicago)
   * @param backfill Whether to include backfill information (currently not implemented)
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing GroupCitationResponse with HTTP 200 status if successful,
   * HTTP 404 with error message if submission not found, or HTTP 400 if invalid parameters
   */
  @GetMapping("/group/{submissionId}")
  public ResponseEntity<?> generateCitationsForGroup(
      @PathVariable Long submissionId,
      @RequestParam(defaultValue = "MLA") String style,
      @RequestParam(defaultValue = "false") boolean backfill,
      HttpServletRequest request) {
    try {
      if (submissionId == null || submissionId <= 0) {
        ErrorResponse error = new ErrorResponse(
            "Invalid Submission ID",
            "Submission ID must be a positive integer. Provided ID: " + submissionId,
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      if (style == null || style.trim().isEmpty()) {
        ErrorResponse error = new ErrorResponse(
            "Invalid Style Parameter",
            "Citation style cannot be null or empty. Supported styles: MLA, APA, CHICAGO",
            HttpStatus.BAD_REQUEST.value(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }

      GroupCitationResponse response = citationService.generateCitationsForGroup(
          submissionId, style, backfill);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      ErrorResponse error = new ErrorResponse(
          "Submission Not Found",
          "No submission found with ID: " + submissionId + ". " + e.getMessage(),
          HttpStatus.NOT_FOUND.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      ErrorResponse error = new ErrorResponse(
          "Citation Generation Error",
          "An unexpected error occurred while generating citations for submission ID "
              + submissionId + ": " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          request.getRequestURI()
      );
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}