package com.columbia.coms4156.citationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "videos")
public class Video extends Source {

    /**
     * The director of the video.
     */
    @Column
    private String director;

    /**
     * The duration of the video in seconds.
     */
    @Column
    private Integer durationSeconds;

    /**
     * The platform where the video is hosted.
     */
    @Column
    private String platform;

    /**
     * The URL of the video.
     */
    @Column
    private String url;

    /**
     * The release year of the video.
     */
    @Column
    private Integer releaseYear;

    /**
     * Default constructor for Video.
     */
    public Video() {
        super();
    }

    /**
     * Constructor for Video with title and author.
     *
     * @param title the title of the video
     * @param author the author of the video
     */
    public Video(String title, String author) {
        super(title, author);
    }

    /**
     * Gets the director of the video.
     *
     * @return the director
     */
    public String getDirector() {
        return director;
    }

    /**
     * Sets the director of the video.
     *
     * @param directorParam the director to set
     */
    public void setDirector(String directorParam) {
        this.director = directorParam;
    }

    /**
     * Gets the duration in seconds.
     *
     * @return the duration in seconds
     */
    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    /**
     * Sets the duration in seconds.
     *
     * @param durationSecondsParam the duration in seconds to set
     */
    public void setDurationSeconds(Integer durationSecondsParam) {
        this.durationSeconds = durationSecondsParam;
    }

    /**
     * Gets the platform where the video is hosted.
     *
     * @return the platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Sets the platform where the video is hosted.
     *
     * @param platformParam the platform to set
     */
    public void setPlatform(String platformParam) {
        this.platform = platformParam;
    }

    /**
     * Gets the URL of the video.
     *
     * @return the URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL of the video.
     *
     * @param urlParam the URL to set
     */
    public void setUrl(String urlParam) {
        this.url = urlParam;
    }

    /**
     * Gets the release year of the video.
     *
     * @return the release year
     */
    public Integer getReleaseYear() {
        return releaseYear;
    }

    /**
     * Sets the release year of the video.
     *
     * @param releaseYearParam the release year to set
     */
    public void setReleaseYear(Integer releaseYearParam) {
        this.releaseYear = releaseYearParam;
    }

    @Override
    public String toString() {
        return "Video{"
                + "id=" + getId()
                + ", title='" + getTitle() + '\''
                + ", author='" + getAuthor() + '\''
                + ", director='" + director + '\''
                + ", durationSeconds=" + durationSeconds
                + ", platform='" + platform + '\''
                + ", url='" + url + '\''
                + ", releaseYear=" + releaseYear
                + '}';
    }
}

