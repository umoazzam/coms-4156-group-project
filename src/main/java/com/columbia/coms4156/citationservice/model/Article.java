package com.columbia.coms4156.citationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "articles")
public class Article extends Source {

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
     */
    public void setJournal(String journalParam) {
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
     */
    public void setVolume(String volumeParam) {
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
     */
    public void setIssue(String issueParam) {
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
     */
    public void setPages(String pagesParam) {
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
     */
    public void setDoi(String doiParam) {
        this.doi = doiParam;
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
     */
    public void setUrl(String urlParam) {
        this.url = urlParam;
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
     */
    public void setPublicationYear(Integer publicationYearParam) {
        this.publicationYear = publicationYearParam;
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

