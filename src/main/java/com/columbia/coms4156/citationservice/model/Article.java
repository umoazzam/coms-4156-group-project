package com.columbia.coms4156.citationservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "articles")
public class Article extends Source {

  // Instance Vairables
    @Column
    private String journal;

    @Column
    private String volume;

    @Column
    private String issue;

    @Column
    private String pages;

    @Column
    private String doi;

    @Column
    private String url;

    @Column
    private Integer publicationYear;

    // Constructors
    public Article() { super(); }

    public Article(String title, String author) {
        super(title, author);
    }

    // Methods
    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", journal='" + journal + '\'' +
                ", volume='" + volume + '\'' +
                ", issue='" + issue + '\'' +
                ", pages='" + pages + '\'' +
                ", doi='" + doi + '\'' +
                ", url='" + url + '\'' +
                ", publicationYear=" + publicationYear +
                '}';
    }
}

