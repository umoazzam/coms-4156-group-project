package com.columbia.coms4156.citationservice.repository;

import com.columbia.coms4156.citationservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Book entity database operations.
 * Extends JpaRepository to provide standard CRUD operations and includes
 * custom query methods for citation management functionality.
 *
 * <p>This repository handles all database interactions for Book entities,
 * supporting the citation service's need to store and retrieve book information
 * for citation generation purposes.</p>
 *
 * @author Citation Service Team
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Finds all books written by a specific author.
     * Performs an exact match on the author field.
     *
     * @param author The author name to search for (case-sensitive)
     * @return A list of books by the specified author, empty list if none found
     * @throws IllegalArgumentException if author is null
     */
    List<Book> findByAuthor(String author);

    /**
     * Finds all books whose title contains the specified text (case-insensitive).
     * Useful for searching books when the exact title is not known.
     *
     * @param title The text to search for within book titles (case-insensitive)
     * @return A list of books with titles containing the search text, empty list if none found
     * @throws IllegalArgumentException if title is null
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Finds all books published in a specific year.
     * Useful for filtering citations by publication date.
     *
     * @param year The publication year to search for
     * @return A list of books published in the specified year, empty list if none found
     * @throws IllegalArgumentException if year is null
     */
    List<Book> findByPublicationYear(Integer year);

    /**
     * Finds a book by its title and author, ignoring case.
     * Useful for detecting duplicates when adding new books.
     *
     * @param title  The title of the book to search for (case-insensitive)
     * @param author The author of the book to search for (case-insensitive)
     * @return An Optional containing the found book, or empty if no match is found
     * @throws IllegalArgumentException if title or author is null
     */
    Optional<Book> findByTitleIgnoreCaseAndAuthorIgnoreCase(String title, String author);
}
