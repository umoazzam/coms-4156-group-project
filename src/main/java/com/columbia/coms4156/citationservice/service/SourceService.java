package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.controller.dto.BulkSourceRequest;
import com.columbia.coms4156.citationservice.controller.dto.SourceDTO;
import com.columbia.coms4156.citationservice.controller.dto.SourceBatchResponse;
import com.columbia.coms4156.citationservice.model.Article;
import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.model.Citation;
import com.columbia.coms4156.citationservice.model.Submission;
import com.columbia.coms4156.citationservice.model.User;
import com.columbia.coms4156.citationservice.model.Video;
import com.columbia.coms4156.citationservice.repository.ArticleRepository;
import com.columbia.coms4156.citationservice.repository.BookRepository;
import com.columbia.coms4156.citationservice.repository.CitationRepository;
import com.columbia.coms4156.citationservice.repository.SubmissionRepository;
import com.columbia.coms4156.citationservice.repository.UserRepository;
import com.columbia.coms4156.citationservice.repository.VideoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

  /**
   * Repository for managing Submission entities used in batch operations.
   */
  @Autowired
  private SubmissionRepository submissionRepository;

  /**
   * Repository for managing Citation link records.
   */
  @Autowired
  private CitationRepository citationRepository;

  /**
   * Repository for managing User entities.
   */
  @Autowired
  private UserRepository userRepository;

  /** ObjectMapper for JSON processing. */
  private final ObjectMapper objectMapper = new ObjectMapper();

  /** Number of seconds in a minute. */
  private static final int SECONDS_IN_MINUTE = 60;

  /** Number of seconds in an hour. */
  private static final int SECONDS_IN_HOUR = 3600;

  /** Constant: number of parts when duration contains hours (hh:mm:ss). */
  private static final int DURATION_PARTS_WITH_HOURS = 3;

  /** Constant: number of parts when duration contains minutes (mm:ss). */
  private static final int DURATION_PARTS_WITH_MINUTES = 2;

  /** Default batch size for bulk operations. */
  private static final int DEFAULT_BATCH_SIZE = 3;

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

  /**
   * Processes a batch of sources: creates a submission group (if submissionId is null)
   * or appends to existing submission, de-duplicates media by title+author
   * (case-insensitive), persists media as needed, and creates Citation records
   * linking media to the submission.
   *
   * @param request the bulk request containing user and sources to process
   * @param submissionId the optional existing submission id to append to
   * @return a SourceBatchResponse containing the submission id and saved citation ids
   */
  @Transactional
  public SourceBatchResponse addOrAppendSources(BulkSourceRequest request, Long submissionId) {
    if (request == null
        || request.getSources() == null
        || request.getSources().isEmpty()) {
      return new SourceBatchResponse(submissionId, new ArrayList<>());
    }

    String username = null;
    if (request.getUser() != null) {
      username = request.getUser().getUsername();
    }

    // Resolve or create Submission
    Submission submission;
    if (submissionId == null) {
      submission = new Submission();
      if (username != null) {
        Optional<User> uOpt = userRepository.findByUsername(username);
        uOpt.ifPresent(submission::setUser);
      }
      submission = submissionRepository.save(submission);
    } else {
      final String msg = "submissionId not found: " + submissionId;
      submission = submissionRepository.findById(submissionId)
          .orElseThrow(() -> new IllegalArgumentException(msg));
    }

    List<String> savedCitationIds = new ArrayList<>();
    // collect errors encountered while processing sources (e.g., unsupported media types)
    List<String> errors = new ArrayList<>();

    for (SourceDTO src : request.getSources()) {
      String rawType = src.getMediaType();
      String type = rawType == null ? "" : rawType.trim().toLowerCase();
      String rawTitle = src.getTitle();
      String title = rawTitle == null ? "" : rawTitle.trim();
      String rawAuthor = src.getAuthor();
      String author = rawAuthor == null ? "" : rawAuthor.trim();

      Long mediaId = null;

      switch (type) {
        case "book":
          // try find by title+author
          Optional<Book> bOpt = bookRepository
              .findByTitleIgnoreCaseAndAuthorIgnoreCase(
                  title,
                  author
              );
          Book book;
          if (bOpt.isPresent()) {
            book = bOpt.get();
          } else {
            book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(src.getIsbn());
            book.setPublisher(src.getPublisher());
            book.setPublicationYear(src.getYear());
            book.setCity(src.getCity());
            book.setEdition(src.getEdition());
            book = bookRepository.save(book);
          }
          mediaId = book.getId();
          break;

        case "article":
          Optional<Article> aOpt = articleRepository
              .findByTitleIgnoreCaseAndAuthorIgnoreCase(
                  title,
                  author
              );
          Article article;
          if (aOpt.isPresent()) {
            article = aOpt.get();
          } else {
            article = new Article();
            article.setTitle(title);
            article.setAuthor(author);
            article.setPublicationYear(src.getYear());
            article.setUrl(src.getUrl());
            article = articleRepository.save(article);
          }
          mediaId = article.getId();
          break;

        case "video":
          Optional<Video> vOpt = videoRepository
              .findByTitleIgnoreCaseAndAuthorIgnoreCase(
                  title,
                  author
              );
          Video video;
          if (vOpt.isPresent()) {
            video = vOpt.get();
          } else {
            video = new Video();
            video.setTitle(title);
            video.setAuthor(author);
            video.setDirector(src.getDirector());
            video.setPlatform(src.getPlatform());
            video.setUrl(src.getUrl());
            video.setReleaseYear(src.getYear());
            video = videoRepository.save(video);
          }
          mediaId = video.getId();
          break;

        default:
          // unknown mediaType -> record an error and skip
          String unsupported = String.format("Unsupported mediaType '%s' for "
                  + "source(title='%s', author='%s')", rawType, title, author);
          errors.add(unsupported);
          errors.add(String.format("MediaType error (book): title='%s', author="
                  + "'%s'", title, author));
          continue;
      }

      // create Citation linking submission -> media
      String userInputJson;
      try {
        userInputJson = objectMapper.writeValueAsString(src);
      } catch (JsonProcessingException e) {
        userInputJson = "{}";
      }

      // avoid duplicate citation for same submission+media+type
      Optional<Citation> existingCitation = citationRepository
          .findBySubmissionIdAndMediaIdAndMediaType(
              submission.getId(),
              mediaId,
              type
          );
      if (existingCitation.isPresent()) {
        savedCitationIds.add(existingCitation.get().getId().toString());
      } else {
        Citation citation = new Citation(submission, userInputJson, mediaId, type);
        submission.addCitation(citation);
        citation = citationRepository.save(citation);
        savedCitationIds.add(citation.getId().toString());
      }
    }

    return new SourceBatchResponse(submission.getId(), savedCitationIds, errors);
  }
}
