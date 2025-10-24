package com.columbia.coms4156.citationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "videos")
public class Video extends Source {

    /**
     * Minimum valid release year.
     */
    private static final int MIN_RELEASE_YEAR = 1888; // First motion picture

    /**
     * Number of years into the future allowed for release dates.
     */
    private static final int FUTURE_YEAR_BUFFER = 10;

    /**
     * Minimum valid duration in seconds.
     */
    private static final int MIN_DURATION_SECONDS = 1;

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
     * @throws IllegalArgumentException if directorParam is blank
     */
    public void setDirector(String directorParam) {
        if (directorParam != null && directorParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Director cannot be blank");
        }
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
     * @throws IllegalArgumentException if durationSecondsParam is less than 1
     */
    public void setDurationSeconds(Integer durationSecondsParam) {
        if (durationSecondsParam != null && durationSecondsParam < MIN_DURATION_SECONDS) {
            throw new IllegalArgumentException("Duration must be at least 1 second");
        }
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
     * @throws IllegalArgumentException if platformParam is blank
     */
    public void setPlatform(String platformParam) {
        if (platformParam != null && platformParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Platform cannot be blank");
        }
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
     * @throws IllegalArgumentException if releaseYearParam is outside valid range
     */
    public void setReleaseYear(Integer releaseYearParam) {
        if (releaseYearParam != null && !isValidReleaseYear(releaseYearParam)) {
            throw new IllegalArgumentException("Release year must be between "
                + MIN_RELEASE_YEAR + " and " + (getCurrentYear() + FUTURE_YEAR_BUFFER));
        }
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

    /**
     * Validates release year.
     * Must be between 1888 (first motion picture) and current year + 10
     *
     * @param year the year to validate
     * @return true if valid release year, false otherwise
     */
    private boolean isValidReleaseYear(Integer year) {
        if (year == null) {
            return true; // null is allowed
        }
        int currentYear = getCurrentYear();
        return year >= MIN_RELEASE_YEAR && year <= (currentYear + FUTURE_YEAR_BUFFER);
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
}

