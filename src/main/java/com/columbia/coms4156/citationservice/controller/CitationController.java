package com.columbia.coms4156.citationservice.controller;

import com.columbia.coms4156.citationservice.model.Book;
import com.columbia.coms4156.citationservice.service.CitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Citation management API endpoints.
 * Provides endpoints for CRUD operations and citation generation for various source types.
 * Currently supports books, with planned expansion to websites, films, and other sources.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CitationController {

    @Autowired
    private CitationService citationService;

    /**
     * Create a new book citation source.
     *
     * @param book The book entity to create with required title and author fields
     * @return ResponseEntity containing the created book with HTTP 201 status,
     *         or HTTP 500 if an error occurs
     */
    @PostMapping("/book")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        try {
            Book savedBook = citationService.saveBook(book);
            return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieve all book citation sources from the database.
     *
     * @return ResponseEntity containing a list of all books with HTTP 200 status,
     *         or HTTP 500 if an error occurs
     */
    @GetMapping("/book")
    public ResponseEntity<List<Book>> getAllBooks() {
        try {
            List<Book> books = citationService.getAllBooks();
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieve a specific book citation source by its unique identifier.
     *
     * @param id The unique identifier of the book to retrieve
     * @return ResponseEntity containing the book with HTTP 200 status if found,
     *         or HTTP 404 if the book doesn't exist
     */
    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = citationService.findBookById(id);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Update an existing book citation source with new information.
     *
     * @param id The unique identifier of the book to update
     * @param book The book entity containing updated information
     * @return ResponseEntity containing the updated book with HTTP 200 status if successful,
     *         HTTP 404 if the book doesn't exist, or HTTP 500 if an error occurs
     */
    @PutMapping("/book/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        try {
            Book updatedBook = citationService.updateBook(id, book);
            if (updatedBook != null) {
                return new ResponseEntity<>(updatedBook, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a book citation source from the database.
     *
     * @param id The unique identifier of the book to delete
     * @return ResponseEntity with HTTP 204 status if successfully deleted,
     *         HTTP 404 if the book doesn't exist, or HTTP 500 if an error occurs
     */
    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            Optional<Book> book = citationService.findBookById(id);
            if (book.isPresent()) {
                citationService.deleteBook(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Generate an MLA format citation for a stored book.
     *
     * @param id The unique identifier of the book to generate citation for
     * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
     *         HTTP 404 with error message if book not found, or HTTP 500 if an error occurs
     */
    @GetMapping("/book/{id}/citation")
    public ResponseEntity<String> generateCitation(@PathVariable Long id) {
        try {
            Optional<Book> book = citationService.findBookById(id);
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
     * Generate an MLA format citation directly from book data without storing it.
     * Useful for quick citation generation without persisting the book information.
     *
     * @param book The book entity containing information for citation generation
     * @return ResponseEntity containing the MLA citation string with HTTP 200 status if successful,
     *         or HTTP 500 with error message if citation generation fails
     */
    @PostMapping("/book/citation")
    public ResponseEntity<String> generateCitationFromData(@Valid @RequestBody Book book) {
        try {
            String citation = citationService.generateMLACitation(book);
            return new ResponseEntity<>(citation, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error generating citation", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
