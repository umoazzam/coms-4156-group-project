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

    /** The city of the source, if applicable. */
    private String city;

    /** The director of the source, if applicable. */
    private String edition;

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
     * @param mediaTypeParam the media type to set
     */
    public void setMediaType(String mediaTypeParam) {
        this.mediaType = mediaTypeParam;
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
     * @param titleParam the title to set
     */
    public void setTitle(String titleParam) {
        this.title = titleParam;
    }

    /**
    * Sets the title of the source.
    *
    * @param cityParam the title to set
    */
    public void setCity(String cityParam) {
        this.city = cityParam;
    }

    /**
    * Sets the title of the source.
    *
    * @param editionParam the title to set
    */
    public void setEdition(String editionParam) {
        this.edition = editionParam;
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
    * Gets the edition of the source.
    *
    * @return the author
    */
    public String getEdition() {
      return edition;
    }

    /**
    * Gets the city of the source.
    *
    * @return the author
    */
    public String getCity() {
      return city;
    }

  /**
     * Sets the author of the source.
     *
     * @param authorParam the author to set
     */
    public void setAuthor(String authorParam) {
        this.author = authorParam;
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
     * @param isbnParam the ISBN to set
     */
    public void setIsbn(String isbnParam) {
        this.isbn = isbnParam;
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
     * @param publisherParam the publisher to set
     */
    public void setPublisher(String publisherParam) {
        this.publisher = publisherParam;
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
     * @param yearParam the year to set
     */
    public void setYear(Integer yearParam) {
        this.year = yearParam;
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
     * @param urlParam the URL to set
     */
    public void setUrl(String urlParam) {
        this.url = urlParam;
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
     * @param accessDateParam the access date to set
     */
    public void setAccessDate(String accessDateParam) {
        this.accessDate = accessDateParam;
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
     * @param platformParam the platform to set
     */
    public void setPlatform(String platformParam) {
        this.platform = platformParam;
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
     * @param durationParam the duration to set
     */
    public void setDuration(String durationParam) {
        this.duration = durationParam;
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
     * @param channelParam the channel to set
     */
    public void setChannel(String channelParam) {
        this.channel = channelParam;
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
     * @param directorParam the director to set
     */
    public void setDirector(String directorParam) {
        this.director = directorParam;
    }
}
