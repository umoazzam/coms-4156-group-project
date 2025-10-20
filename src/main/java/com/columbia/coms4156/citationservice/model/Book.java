package com.columbia.coms4156.citationservice.model;

import jakarta.persistence.*;

/**
 * Model class representing a Book entity for citation generation.
 * This entity stores all necessary information for creating properly formatted
 * citations in various academic styles (currently supporting MLA format).
 *
 * <p>The Book entity includes both required fields (title, author) and optional
 * fields that enhance citation completeness and accuracy.</p>
 *
 * @author Citation Service Team
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "books")
public class Book extends Source {

    // Instance Variables
    /**
     * The publisher of the book.
     * Optional field that enhances citation completeness.
     */
    @Column
    private String publisher;

    /**
     * The year the book was published.
     * Optional field used in citation formatting.
     */
    @Column
    private Integer publicationYear;

    /**
     * The city where the book was published.
     * Optional field used in some citation styles.
     */
    @Column
    private String city;

    /**
     * The edition of the book (e.g., "2nd edition", "Revised").
     * Optional field for distinguishing between different editions.
     */
    @Column
    private String edition;

    /**
     * The International Standard Book Number (ISBN) of the book.
     * Optional field for unique book identification.
     */
    @Column
    private String isbn;

    // Constructors
    /**
     * Default constructor for JPA entity creation.
     * Creates a new Book instance with no initial values.
     */
    public Book() { super(); }

    /**
     * Constructor for creating a Book with required fields.
     *
     * @param title The title of the book (required)
     * @param author The author of the book (required)
     */
    public Book(String title, String author) {
        super(title, author);
    }

    // Methods
    /**
     * Gets the publisher of the book.
     *
     * @return The book's publisher, or null if not specified
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the publisher of the book.
     *
     * @param publisher The publisher to set (optional field)
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Gets the publication year of the book.
     *
     * @return The book's publication year, or null if not specified
     */
    public Integer getPublicationYear() {
        return publicationYear;
    }

    /**
     * Sets the publication year of the book.
     *
     * @param publicationYear The publication year to set (optional field)
     */
    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    /**
     * Gets the publication city of the book.
     *
     * @return The book's publication city, or null if not specified
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the publication city of the book.
     *
     * @param city The publication city to set (optional field)
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the edition information of the book.
     *
     * @return The book's edition, or null if not specified
     */
    public String getEdition() {
        return edition;
    }

    /**
     * Sets the edition information of the book.
     *
     * @param edition The edition to set (optional field)
     */
    public void setEdition(String edition) {
        this.edition = edition;
    }

    /**
     * Gets the ISBN of the book.
     *
     * @return The book's ISBN, or null if not specified
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the ISBN of the book.
     *
     * @param isbn The ISBN to set (optional field)
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Returns a string representation of the Book object.
     * Includes all fields for debugging and logging purposes.
     *
     * @return A string representation of the Book
     */
    @Override
    public String toString() {
        return "Book{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publicationYear=" + publicationYear +
                ", city='" + city + '\'' +
                ", edition='" + edition + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
