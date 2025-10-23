package com.columbia.coms4156.citationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "articles")
public class Article extends Source {

    /**
     * Minimum valid publication year.
     */
    private static final int MIN_PUBLICATION_YEAR = 1000;

    /**
     * Number of years into the future allowed for publication dates.
     */
    private static final int FUTURE_YEAR_BUFFER = 10;

    /**
     * The journal where the article was published.
     */
    @Column
    private String journal;

    /**
     * The volume number of the journal.
     */
    @Column
    private String volume;

    /**
     * The issue number of the journal.
     */
    @Column
    private String issue;

    /**
     * The page numbers of the article.
     */
    @Column
    private String pages;

    /**
     * The DOI (Digital Object Identifier) of the article.
     */
    @Column
    private String doi;

    /**
     * The URL of the article.
     */
    @Column
    private String url;

    /**
     * The publication year of the article.
     */
    @Column
    private Integer publicationYear;

    /**
     * Default constructor for Article.
     */
    public Article() {
        super();
    }

    /**
     * Constructor for Article with title and author.
     *
     * @param title the title of the article
     * @param author the author of the article
     */
    public Article(String title, String author) {
        super(title, author);
    }

    /**
     * Gets the journal name.
     *
     * @return the journal name
     */
    public String getJournal() {
        return journal;
    }

    /**
     * Sets the journal name.
     *
     * @param journalParam the journal name to set
     * @throws IllegalArgumentException if journalParam is null or blank
     */
    public void setJournal(String journalParam) {
        if (journalParam != null && journalParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Journal name cannot be blank");
        }
        this.journal = journalParam;
    }

    /**
     * Gets the volume number.
     *
     * @return the volume number
     */
    public String getVolume() {
        return volume;
    }

    /**
     * Sets the volume number.
     *
     * @param volumeParam the volume number to set
     * @throws IllegalArgumentException if volumeParam is null or blank
     */
    public void setVolume(String volumeParam) {
        if (volumeParam != null && volumeParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Volume number cannot be blank");
        }
        this.volume = volumeParam;
    }

    /**
     * Gets the issue number.
     *
     * @return the issue number
     */
    public String getIssue() {
        return issue;
    }

    /**
     * Sets the issue number.
     *
     * @param issueParam the issue number to set
     * @throws IllegalArgumentException if issueParam is null or blank
     */
    public void setIssue(String issueParam) {
        if (issueParam != null && issueParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Issue number cannot be blank");
        }
        this.issue = issueParam;
    }

    /**
     * Gets the page numbers.
     *
     * @return the page numbers
     */
    public String getPages() {
        return pages;
    }

    /**
     * Sets the page numbers.
     *
     * @param pagesParam the page numbers to set
     * @throws IllegalArgumentException if pagesParam is null or blank
     */
    public void setPages(String pagesParam) {
        if (pagesParam != null && pagesParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Page numbers cannot be blank");
        }
        this.pages = pagesParam;
    }

    /**
     * Gets the DOI.
     *
     * @return the DOI
     */
    public String getDoi() {
        return doi;
    }

    /**
     * Sets the DOI.
     *
     * @param doiParam the DOI to set
     * @throws IllegalArgumentException if doiParam is blank or doesn't follow DOI format
     */
    public void setDoi(String doiParam) {
        if (doiParam != null && doiParam.trim().isEmpty()) {
            throw new IllegalArgumentException("DOI cannot be blank");
        }
        if (doiParam != null && !isValidDoi(doiParam)) {
            throw new IllegalArgumentException(
                "DOI must follow standard format (e.g., 10.1000/123456)");
        }
        this.doi = doiParam;
    }

    /**
     * Validates DOI format.
     * DOI format: starts with "10." followed by a registrant code and a suffix
     *
     * @param doiValue the DOI to validate
     * @return true if valid DOI format, false otherwise
     */
    private boolean isValidDoi(String doiValue) {
        if (doiValue == null || doiValue.trim().isEmpty()) {
            return false;
        }
        // DOI pattern: 10. followed by registrant code (digits) / suffix
        return doiValue.matches("^10\\.[0-9]{4,}/[\\S]+$");
    }

    /**
     * Gets the URL.
     *
     * @return the URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL.
     *
     * @param urlParam the URL to set
     * @throws IllegalArgumentException if urlParam is blank or doesn't follow URL format
     */
    public void setUrl(String urlParam) {
        if (urlParam != null && urlParam.trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be blank");
        }
        if (urlParam != null && !isValidUrl(urlParam)) {
            throw new IllegalArgumentException("URL must be a valid HTTP or HTTPS URL");
        }
        this.url = urlParam;
    }

    /**
     * Validates URL format.
     * Accepts HTTP and HTTPS URLs
     *
     * @param urlValue the URL to validate
     * @return true if valid URL format, false otherwise
     */
    private boolean isValidUrl(String urlValue) {
        if (urlValue == null || urlValue.trim().isEmpty()) {
            return false;
        }
        // URL pattern: starts with http:// or https:// followed by valid characters
        return urlValue.matches("^https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+$");
    }

    /**
     * Gets the publication year.
     *
     * @return the publication year
     */
    public Integer getPublicationYear() {
        return publicationYear;
    }

    /**
     * Sets the publication year.
     *
     * @param publicationYearParam the publication year to set
     * @throws IllegalArgumentException if publicationYearParam is negative or unreasonable
     */
    public void setPublicationYear(Integer publicationYearParam) {
        if (publicationYearParam != null && !isValidPublicationYear(publicationYearParam)) {
            throw new IllegalArgumentException("Publication year must be between "
                + MIN_PUBLICATION_YEAR + " and " + (getCurrentYear() + FUTURE_YEAR_BUFFER));
        }
        this.publicationYear = publicationYearParam;
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

    @Override
    public String toString() {
        return "Article{"
                + "id=" + getId()
                + ", title='" + getTitle() + '\''
                + ", author='" + getAuthor() + '\''
                + ", journal='" + journal + '\''
                + ", volume='" + volume + '\''
                + ", issue='" + issue + '\''
                + ", pages='" + pages + '\''
                + ", doi='" + doi + '\''
                + ", url='" + url + '\''
                + ", publicationYear=" + publicationYear
                + '}';
    }
}

