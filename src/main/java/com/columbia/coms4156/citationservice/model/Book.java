package com.columbia.coms4156.citationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

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

    /**
     * Minimum valid publication year.
     */
    private static final int MIN_PUBLICATION_YEAR = 1000;

    /**
     * Number of years into the future allowed for publication dates.
     */
    private static final int FUTURE_YEAR_BUFFER = 10;

    /**
     * Length of ISBN-10.
     */
    private static final int ISBN_10_LENGTH = 10;

    /**
     * Length of ISBN-13.
     */
    private static final int ISBN_13_LENGTH = 13;

    /**
     * ISBN-10 checksum modulus.
     */
    private static final int ISBN_10_MODULUS = 11;

    /**
     * ISBN-10 X character value.
     */
    private static final int ISBN_10_X_VALUE = 10;

    /**
     * ISBN-13 multiplier for odd positions.
     */
    private static final int ISBN_13_ODD_MULTIPLIER = 3;

    /**
     * General base 10 value.
     */
    private static final int BASE_10 = 10;

    /**
     * ISBN-10 checksum calculation limit (excludes check digit).
     */
    private static final int ISBN_10_CHECKSUM_LIMIT = 9;

    /**
     * ISBN-13 checksum calculation limit (excludes check digit).
     */
    private static final int ISBN_13_CHECKSUM_LIMIT = 12;

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
    public Book() {
        super();
    }

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
     * @param publisherParam The publisher to set (optional field)
     * @throws IllegalArgumentException if publisherParam is blank
     */
    public void setPublisher(String publisherParam) {
        if (publisherParam != null && publisherParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Publisher cannot be blank");
        }
        this.publisher = publisherParam;
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
     * @param publicationYearParam The publication year to set (optional field)
     * @throws IllegalArgumentException if publicationYearParam is outside valid range
     */
    public void setPublicationYear(Integer publicationYearParam) {
        if (publicationYearParam != null && !isValidPublicationYear(publicationYearParam)) {
            throw new IllegalArgumentException("Publication year must be between "
                + MIN_PUBLICATION_YEAR + " and " + (getCurrentYear() + FUTURE_YEAR_BUFFER));
        }
        this.publicationYear = publicationYearParam;
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
     * @param cityParam The publication city to set (optional field)
     * @throws IllegalArgumentException if cityParam is blank
     */
    public void setCity(String cityParam) {
        if (cityParam != null && cityParam.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be blank");
        }
        this.city = cityParam;
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
     * @param editionParam The edition to set (optional field)
     * @throws IllegalArgumentException if editionParam is blank
     */
    public void setEdition(String editionParam) {
        if (editionParam != null && editionParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Edition cannot be blank");
        }
        this.edition = editionParam;
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
     * @param isbnParam The ISBN to set (optional field)
     * @throws IllegalArgumentException if isbnParam is blank or invalid format
     */
    public void setIsbn(String isbnParam) {
        if (isbnParam != null && isbnParam.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be blank");
        }
        if (isbnParam != null && !isValidIsbn(isbnParam)) {
            throw new IllegalArgumentException("ISBN must be a valid ISBN-10 or ISBN-13 format");
        }
        this.isbn = isbnParam;
    }

    /**
     * Returns a string representation of the Book object.
     * Includes all fields for debugging and logging purposes.
     *
     * @return A string representation of the Book
     */
    @Override
    public String toString() {
        return "Book{"
                + "id=" + getId()
                + ", title='" + getTitle() + '\''
                + ", author='" + getAuthor() + '\''
                + ", publisher='" + publisher + '\''
                + ", publicationYear=" + publicationYear
                + ", city='" + city + '\''
                + ", edition='" + edition + '\''
                + ", isbn='" + isbn + '\''
                + '}';
    }

    /**
     * Validates publication year.
     * Must be between 1000 and current year + 10 (to allow for future publications)
     *
     * @param year the year to validate
     * @return true if valid publication year, false otherwise
     */
    private boolean isValidPublicationYear(Integer year) {
        if (year == null) {
            return true; // null is allowed
        }
        int currentYear = getCurrentYear();
        return year >= MIN_PUBLICATION_YEAR && year <= (currentYear + FUTURE_YEAR_BUFFER);
    }

    /**
     * Gets the current year for validation purposes.
     *
     * @return the current year
     */
    private int getCurrentYear() {
        return java.time.Year.now().getValue();
    }

    /**
     * Validates ISBN format and checksum.
     * Supports both ISBN-10 and ISBN-13 formats
     *
     * @param isbnValue the ISBN to validate
     * @return true if valid ISBN format and checksum, false otherwise
     */
    private boolean isValidIsbn(String isbnValue) {
        if (isbnValue == null || isbnValue.trim().isEmpty()) {
            return false;
        }

        // Remove hyphens and spaces
        String cleanIsbn = isbnValue.replaceAll("[\\s\\-]", "");

        if (cleanIsbn.length() == ISBN_10_LENGTH) {
            return isValidIsbn10(cleanIsbn);
        } else if (cleanIsbn.length() == ISBN_13_LENGTH) {
            return isValidIsbn13(cleanIsbn);
        }

        return false;
    }

    /**
     * Validates ISBN-10 format and checksum.
     *
     * @param isbn10 the ISBN-10 to validate (without hyphens)
     * @return true if valid ISBN-10, false otherwise
     */
    private boolean isValidIsbn10(String isbn10) {
        if (!isbn10.matches("^[0-9]{9}[0-9X]$")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < ISBN_10_CHECKSUM_LIMIT; i++) {
            int digit = Character.getNumericValue(isbn10.charAt(i));
            sum += (i + 1) * digit;
        }

        char checkChar = isbn10.charAt(ISBN_10_CHECKSUM_LIMIT);
        int checkDigit = (checkChar == 'X') ? ISBN_10_X_VALUE
            : Character.getNumericValue(checkChar);

        return (sum % ISBN_10_MODULUS) == checkDigit;
    }

    /**
     * Validates ISBN-13 format and checksum.
     *
     * @param isbn13 the ISBN-13 to validate (without hyphens)
     * @return true if valid ISBN-13, false otherwise
     */
    private boolean isValidIsbn13(String isbn13) {
        if (!isbn13.matches("^[0-9]{13}$")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < ISBN_13_CHECKSUM_LIMIT; i++) {
            int digit = Character.getNumericValue(isbn13.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * ISBN_13_ODD_MULTIPLIER;
        }

        int checkDigit = Character.getNumericValue(isbn13.charAt(ISBN_13_CHECKSUM_LIMIT));
        int calculatedCheck = (BASE_10 - (sum % BASE_10)) % BASE_10;

        return checkDigit == calculatedCheck;
    }
}
