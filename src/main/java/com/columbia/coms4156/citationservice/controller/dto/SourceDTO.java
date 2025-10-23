package com.columbia.coms4156.citationservice.controller.dto;

/**
 * DTO representing a source.
 *
 * <p>Contains metadata about a source, such as media type, title, author, and more.</p>
 */
public class SourceDTO {
    /** The type of media (e.g., book, website, film). */
    private String mediaType;

    /** The title of the source. */
    private String title;

    /** The author of the source. */
    private String author;

    /** The ISBN of the source, if applicable. */
    private String isbn;

    /** The publisher of the source. */
    private String publisher;

    /** The year of publication. */
    private Integer year;

    /** The URL of the source, if applicable. */
    private String url;

    /** The access date for the source, if applicable. */
    private String accessDate;

    /** The platform where the source is hosted, if applicable. */
    private String platform;

    /** The duration of the source, if applicable. */
    private String duration;

    /** The channel associated with the source, if applicable. */
    private String channel;

    /** The director of the source, if applicable. */
    private String director;

    /**
     * Default constructor for SourceDTO.
     */
    public SourceDTO() {}

    /**
     * Gets the media type.
     *
     * @return the media type
     */
    public String getMediaType() {
        return this.mediaType;
    }

    /**
     * Sets the media type.
     *
     * @param mediaType the media type to set
     */
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * Gets the title of the source.
     *
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title of the source.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author of the source.
     *
     * @return the author
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Sets the author of the source.
     *
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the ISBN of the source.
     *
     * @return the ISBN
     */
    public String getIsbn() {
        return this.isbn;
    }

    /**
     * Sets the ISBN of the source.
     *
     * @param isbn the ISBN to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets the publisher of the source.
     *
     * @return the publisher
     */
    public String getPublisher() {
        return this.publisher;
    }

    /**
     * Sets the publisher of the source.
     *
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Gets the year of publication.
     *
     * @return the year of publication
     */
    public Integer getYear() {
        return this.year;
    }

    /**
     * Sets the year of publication.
     *
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * Gets the URL of the source.
     *
     * @return the URL
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Sets the URL of the source.
     *
     * @param url the URL to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the access date for the source.
     *
     * @return the access date
     */
    public String getAccessDate() {
        return this.accessDate;
    }

    /**
     * Sets the access date for the source.
     *
     * @param accessDate the access date to set
     */
    public void setAccessDate(String accessDate) {
        this.accessDate = accessDate;
    }

    /**
     * Gets the platform where the source is hosted.
     *
     * @return the platform
     */
    public String getPlatform() {
        return this.platform;
    }

    /**
     * Sets the platform where the source is hosted.
     *
     * @param platform the platform to set
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * Gets the duration of the source.
     *
     * @return the duration
     */
    public String getDuration() {
        return this.duration;
    }

    /**
     * Sets the duration of the source.
     *
     * @param duration the duration to set
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * Gets the channel associated with the source.
     *
     * @return the channel
     */
    public String getChannel() {
        return this.channel;
    }

    /**
     * Sets the channel associated with the source.
     *
     * @param channel the channel to set
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Gets the director of the source.
     *
     * @return the director
     */
    public String getDirector() {
        return this.director;
    }

    /**
     * Sets the director of the source.
     *
     * @param director the director to set
     */
    public void setDirector(String director) {
        this.director = director;
    }
}
