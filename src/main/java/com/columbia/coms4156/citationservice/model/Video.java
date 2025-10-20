package com.columbia.coms4156.citationservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "videos")
public class Video extends Source {

    // Instance Variables
    @Column
    private String director;

    @Column
    private Integer durationSeconds;

    @Column
    private String platform;

    @Column
    private String url;

    @Column
    private Integer releaseYear;

    // Constructors
    public Video() { super(); }

    public Video(String title, String author) {
        super(title, author);
    }

    // Methods
    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", director='" + director + '\'' +
                ", durationSeconds=" + durationSeconds +
                ", platform='" + platform + '\'' +
                ", url='" + url + '\'' +
                ", releaseYear=" + releaseYear +
                '}';
    }
}

