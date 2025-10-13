package com.columbia.coms4156.citationservice.service;

import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for citation-related business logic and citation generation.
 * Handles CRUD operations for various citation types and citation formatting.
 * Currently supports books, with planned expansion to websites, films, and other sources.
 */
@Service
public class CitationService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Save a new book to the database.
     *
     * @param book The book entity to save
     * @return The saved book entity with generated ID
     * @throws IllegalArgumentException if book is null or invalid
     */
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * Find a book by its ID.
     *
     * @param id The unique identifier of the book
     * @return An Optional containing the book if found, empty otherwise
     * @throws IllegalArgumentException if id is null
     */
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * Get all books from the database.
     *
     * @return A list of all books in the database
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Delete a book by its ID.
     *
     * @param id The unique identifier of the book to delete
     * @throws IllegalArgumentException if id is null
     */
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    /**
     * Update an existing book with new information.
     *
     * @param id The unique identifier of the book to update
     * @param updatedBook The book entity containing updated information
     * @return The updated book entity if found, null if not found
     * @throws IllegalArgumentException if id or updatedBook is null
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

    /**
     * Generate MLA citation for a book.
     * Follows MLA 8th edition format: Author. Title. Publisher, Year.
     *
     * @param book The book entity to generate citation for
     * @return A properly formatted MLA citation string
     * @throws IllegalArgumentException if book is null
     */
    public String generateMLACitation(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        StringBuilder citation = new StringBuilder();

        // Author (Last, First)
        if (book.getAuthor() != null && !book.getAuthor().trim().isEmpty()) {
            citation.append(formatAuthorName(book.getAuthor())).append(". ");
        }

        // Title (italicized - represented with underscores for plain text)
        if (book.getTitle() != null && !book.getTitle().trim().isEmpty()) {
            citation.append("_").append(book.getTitle()).append("_. ");
        }

        // Publisher
        if (book.getPublisher() != null && !book.getPublisher().trim().isEmpty()) {
            citation.append(book.getPublisher());
            if (book.getPublicationYear() != null) {
                citation.append(", ");
            } else {
                citation.append(". ");
            }
        }

        // Publication Year
        if (book.getPublicationYear() != null) {
            citation.append(book.getPublicationYear()).append(".");
        }

        return citation.toString().trim();
    }

    /**
     * Format author name for MLA citation (Last, First).
     * Handles simple "First Last" format conversion.
     *
     * @param author The author name in "First Last" format
     * @return The author name formatted as "Last, First" for MLA citation
     * @throws IllegalArgumentException if author is null or empty
     */
    private String formatAuthorName(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }

        // Simple implementation: assumes "First Last" format
        String[] parts = author.trim().split("\\s+");
        if (parts.length >= 2) {
            String firstName = parts[0];
            String lastName = parts[parts.length - 1];
            return lastName + ", " + firstName;
        }
        return author; // Return as-is if format is unclear
    }
}
