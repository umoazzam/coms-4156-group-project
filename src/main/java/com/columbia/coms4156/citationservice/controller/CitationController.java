package com.columbia.coms4156.citationservice.controller;

import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.CitationResponse;
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
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if book not found, or HTTP 500 if an error occurs
   */
  @GetMapping("/book/{id}")
  public ResponseEntity<String> generateBookCitation(@PathVariable Long id) {
    try {
      Optional<Book> book = sourceService.findBookById(id);
      if (book.isPresent()) {
        String citation = citationService.generateMLACitation(book.get());
        return new ResponseEntity<>(citation, HttpStatus.OK);
      } else {
        return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      return new ResponseEntity<>("Error generating citation", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Generate an MLA format citation given a book.
   *
   * @param book The book object to generate a citation for. This is an Ad-Hoc creation and the
   *             book will not be saved.
   * @param style The citation style (MLA, APA, Chicago)
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if book not found, or HTTP 500 if an error occurs
   */
  @PostMapping("/book")
  public ResponseEntity<String> generateBookCitationFromData(
      @Valid @RequestBody Book book,
      @RequestParam(defaultValue = "MLA") String style) {
    try {
      String citation = citationService.generateCitationByStyle(book, style);
      return new ResponseEntity<>(citation, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error generating citation", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // --- Video Endpoints ---
   /**
   * Generate an MLA format citation for a stored video.
   *
   * @param id The unique identifier of the video to generate citation for
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if video not found, or HTTP 500 if an error occurs
   */
  @GetMapping("/video/{id}")
  public ResponseEntity<String> generateVideoCitation(@PathVariable Long id) {
    try {
      Optional<Video> video = sourceService.findVideoById(id);
      if (video.isPresent()) {
        String citation = citationService.generateMLACitation(video.get());
        return new ResponseEntity<>(citation, HttpStatus.OK);
      }
      return new ResponseEntity<>("Video not found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>("Error generating citation", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Generate an MLA format citation given a video.
   *
   * @param video The video object to generate a citation for. This is an Ad-Hoc creation and the
   *             video will not be saved.
   * @param style The citation style (MLA, APA, Chicago)
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if video not found, or HTTP 500 if an error occurs
   */
  @PostMapping("/video/citation")
  public ResponseEntity<String> generateVideoCitationFromData(
      @Valid @RequestBody Video video,
      @RequestParam(defaultValue = "MLA") String style) {
    try {
      String citation = citationService.generateCitationByStyle(video, style);
      return new ResponseEntity<>(citation, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error generating citation", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // --- Article Endpoints ---
  /**
   * Generate an MLA format citation for a stored article.
   *
   * @param id The unique identifier of the article to generate citation for
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if article is not found, or HTTP 500 if an error occurs
   */
  @GetMapping("/article/{id}/citation")
  public ResponseEntity<String> generateArticleCitation(@PathVariable Long id) {
    try {
      Optional<Article> article = sourceService.findArticleById(id);
      if (article.isPresent()) {
        String citation = citationService.generateMLACitation(article.get());
        return new ResponseEntity<>(citation, HttpStatus.OK);
      }
      return new ResponseEntity<>("Article not found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>("Error generating citation", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Generate an MLA format citation given an article.
   *
   * @param article The article object to generate a citation for. This is an Ad-Hoc creation
   *                and the article will not be saved.
   * @param style The citation style (MLA, APA, Chicago)
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if article not found, or HTTP 500 if an error occurs
   */
  @PostMapping("/article/citation")
  public ResponseEntity<String> generateArticleCitationFromData(
      @Valid @RequestBody Article article,
      @RequestParam(defaultValue = "MLA") String style) {
    try {
      String citation = citationService.generateCitationByStyle(article, style);
      return new ResponseEntity<>(citation, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error generating citation", HttpStatus.INTERNAL_SERVER_ERROR);
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
   * @return ResponseEntity containing CitationResponse with HTTP 200 status if successful,
   * HTTP 404 with error message if source not found, or HTTP 400 if invalid parameters
   */
  @GetMapping("/source/{sourceId}")
  public ResponseEntity<CitationResponse> generateCitationForSource(
      @PathVariable Long sourceId,
      @RequestParam(defaultValue = "MLA") String style,
      @RequestParam(defaultValue = "false") boolean backfill) {
    try {
      CitationResponse response = citationService.generateCitationForSource(
          sourceId, style, backfill);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * UPDATE NEEDED FOR NEW API FLOW
   * Generate citations for all sources in a submission group.
   *
   * @param submissionId The unique identifier of the submission group
   * @param style The citation style (MLA, APA, Chicago)
   * @param backfill Whether to include backfill information (currently not implemented)
   * @return ResponseEntity containing GroupCitationResponse with HTTP 200 status if successful,
   * HTTP 404 with error message if submission not found, or HTTP 400 if invalid parameters
   */
  @GetMapping("/group/{submissionId}")
  public ResponseEntity<GroupCitationResponse> generateCitationsForGroup(
      @PathVariable Long submissionId,
      @RequestParam(defaultValue = "MLA") String style,
      @RequestParam(defaultValue = "false") boolean backfill) {
    try {
      GroupCitationResponse response = citationService.generateCitationsForGroup(
          submissionId, style, backfill);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}