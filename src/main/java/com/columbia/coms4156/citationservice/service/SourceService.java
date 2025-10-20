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

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private VideoRepository videoRepository;

  @Autowired
  private ArticleRepository articleRepository;

  // --- Book Methods ---
  public Book saveBook(Book book) {
    return bookRepository.save(book);
  }

  public Optional<Book> findBookById(Long id) {
    return bookRepository.findById(id);
  }

  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  public void deleteBook(Long id) {
    bookRepository.deleteById(id);
  }

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
  public Video saveVideo(Video video) {
    return videoRepository.save(video);
  }

  public Optional<Video> findVideoById(Long id) {
    return videoRepository.findById(id);
  }

  public List<Video> getAllVideos() {
    return videoRepository.findAll();
  }

  public void deleteVideo(Long id) {
    videoRepository.deleteById(id);
  }

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
  public Article saveArticle(Article article) {
    return articleRepository.save(article);
  }

  public Optional<Article> findArticleById(Long id) {
    return articleRepository.findById(id);
  }

  public List<Article> getAllArticles() {
    return articleRepository.findAll();
  }

  public void deleteArticle(Long id) {
    articleRepository.deleteById(id);
  }

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
