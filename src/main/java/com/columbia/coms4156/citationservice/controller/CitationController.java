package com.columbia.coms4156.citationservice.controller;

import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.service.CitationService;
import com.columbia.coms4156.citationservice.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

  @Autowired
  private CitationService citationService;
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
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if book not found, or HTTP 500 if an error occurs
   */
  @PostMapping("/book")
  public ResponseEntity<String> generateBookCitationFromData(@Valid @RequestBody Book book) {
    try {
      String citation = citationService.generateMLACitation(book);
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
   * Generate an MLA format citation given a book.
   *
   * @param video The video object to generate a citation for. This is an Ad-Hoc creation and the 
   *             video will not be saved.
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if video not found, or HTTP 500 if an error occurs
   */
  @PostMapping("/video/citation")
  public ResponseEntity<String> generateVideoCitationFromData(@Valid @RequestBody Video video) {
    try {
      String citation = citationService.generateMLACitation(video);
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
   * Generate an MLA format citation given a article.
   *
   * @param video The video object to generate a citation for. This is an Ad-Hoc creation and the 
   *             article will not be saved.
   * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
   * HTTP 404 with error message if video not found, or HTTP 500 if an error occurs
   */
  @PostMapping("/article/citation")
  public ResponseEntity<String> generateArticleCitationFromData(@Valid @RequestBody Article article) {
    try {
      String citation = citationService.generateMLACitation(article);
      return new ResponseEntity<>(citation, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error generating citation", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}