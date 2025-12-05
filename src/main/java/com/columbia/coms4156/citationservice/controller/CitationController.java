package com.columbia.coms4156.citationservice.controller;

import com.columbia.coms4156.citationservice.exception.ResourceNotFoundException;
import com.columbia.coms4156.citationservice.exception.ValidationException;
import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.CitationResponse;
import com.columbia.coms4156.citationservice.model.GroupCitationResponse;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.service.CitationService;
import com.columbia.coms4156.citationservice.service.SourceService;
import com.columbia.coms4156.citationservice.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * REST Controller for Citation creation API endpoints.
 * Provides endpoints for citation generation for various source types and styles.
 * Currently, supports ad-hoc MLA format generation for books, videos, and articles
 * w/o backfill functionality. For style & backfill selection,
 * see the /{citationId} & /group/{submissionId} endpoints below.
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

  /**
   * Validates that a style parameter is not null or empty.
   *
   * @param style the style to validate
   * @throws ValidationException if the style is invalid
   */
  private void validateStyle(String style) {
    if (style == null || style.trim().isEmpty()) {
      throw new ValidationException(
          "Citation style cannot be null or empty. Supported styles: MLA, APA, CHICAGO");
    }
  }

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
    validateId(id, "Book");

    Book book = sourceService.findBookById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "No book found with ID: " + id + ". Please verify the book ID and try again."));

    String citation = citationService.generateMLACitation(book);
    return ResponseUtil.ok(citation);
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
    if (book == null) {
      throw new ValidationException(
          "Request body cannot be null. Please provide book information.");
    }

    validateStyle(style);

    String citation = citationService.generateCitationByStyle(book, style);
    return ResponseUtil.ok(citation);
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
    validateId(id, "Video");

    Video video = sourceService.findVideoById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "No video found with ID: " + id + ". Please verify the video ID and try again."));

    String citation = citationService.generateMLACitation(video);
    return ResponseUtil.ok(citation);
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
  @PostMapping("/video")
  public ResponseEntity<?> generateVideoCitationFromData(
      @Valid @RequestBody Video video,
      @RequestParam(defaultValue = "MLA") String style,
      HttpServletRequest request) {
    if (video == null) {
      throw new ValidationException(
          "Request body cannot be null. Please provide video information.");
    }

    validateStyle(style);

    String citation = citationService.generateCitationByStyle(video, style);
    return ResponseUtil.ok(citation);
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
  @GetMapping("/article/{id}")
  public ResponseEntity<?> generateArticleCitation(@PathVariable Long id,
                                                   HttpServletRequest request) {
    validateId(id, "Article");

    Article article = sourceService.findArticleById(id)
        .orElseThrow(() -> new ResourceNotFoundException(
            "No article found with ID: " + id + ". Please verify the article ID and try again."));

    String citation = citationService.generateMLACitation(article);
    return ResponseUtil.ok(citation);
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
  @PostMapping("/article")
  public ResponseEntity<?> generateArticleCitationFromData(
      @Valid @RequestBody Article article,
      @RequestParam(defaultValue = "MLA") String style,
      HttpServletRequest request) {
    if (article == null) {
      throw new ValidationException(
          "Request body cannot be null. Please provide article information.");
    }

    validateStyle(style);

    String citation = citationService.generateCitationByStyle(article, style);
    return ResponseUtil.ok(citation);
  }

  // --- General Use Endpoints ---
  /**
   * Generate citation for a single source from a submission group generated
   * from the '/api/source/sources' endpoint. This endpoint will return CitationObjects
   * describing the media type and respective media id. You may use this information,
   * alongside parameters to specify style and backfill options,
   * to generate singular citations for your submitted sources.
   * Otherwise, you may select the respective media type and generate them
   * using the APIs above (ex. /api/cite/article/{id})
   *
   * @param citationId The unique identifier of the source to generate citation for.
   * @param style The citation style (MLA, APA, Chicago)
   * @param backfill Whether to include backfill information (currently not implemented)
   * @param request The HTTP request object for error context
   * @return ResponseEntity containing CitationResponse with HTTP 200 status if successful,
   * HTTP 404 with error message if source not found, or HTTP 400 if invalid parameters
   */
  @GetMapping("/{citationId}")
  public ResponseEntity<?> generateCitationForSource(
      @PathVariable Long citationId,
      @RequestParam(defaultValue = "MLA") String style,
      @RequestParam(defaultValue = "false") boolean backfill,
      HttpServletRequest request) {
    validateId(citationId, "CitationId");
    validateStyle(style);

    CitationResponse response = citationService.generateCitationForSource(
            citationId, style, backfill);
    return ResponseUtil.ok(response);
  }

  /**
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
    validateId(submissionId, "Submission");
    validateStyle(style);

    GroupCitationResponse response = citationService.generateCitationsForGroup(
        submissionId, style, backfill);
    return ResponseUtil.ok(response);
  }
}