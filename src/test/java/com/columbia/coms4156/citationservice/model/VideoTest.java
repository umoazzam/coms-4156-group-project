package com.columbia.coms4156.citationservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Video class.
 * Tests all setter methods and their parameter validation.
 */
class VideoTest {

    private Video video;

    @BeforeEach
    void setUp() {
        video = new Video();
    }

    @Test
    void testDefaultConstructor() {
        Video newVideo = new Video();
        assertNotNull(newVideo);
        assertNull(newVideo.getDirector());
        assertNull(newVideo.getDurationSeconds());
        assertNull(newVideo.getPlatform());
        assertNull(newVideo.getUrl());
        assertNull(newVideo.getReleaseYear());
    }

    @Test
    void testParameterizedConstructor() {
        String title = "Test Video";
        String author = "Test Creator";
        Video newVideo = new Video(title, author);

        assertNotNull(newVideo);
        assertEquals(title, newVideo.getTitle());
        assertEquals(author, newVideo.getAuthor());
    }

    // Director setter tests
    @Test
    void testSetDirectorValid() {
        String validDirector = "Christopher Nolan";
        video.setDirector(validDirector);
        assertEquals(validDirector, video.getDirector());
    }

    @Test
    void testSetDirectorNull() {
        assertDoesNotThrow(() -> video.setDirector(null));
        assertNull(video.getDirector());
    }

    @Test
    void testSetDirectorBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setDirector("   ")
        );
        assertEquals("Director cannot be blank", exception.getMessage());
    }

    @Test
    void testSetDirectorEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setDirector("")
        );
        assertEquals("Director cannot be blank", exception.getMessage());
    }

    // Duration setter tests
    @Test
    void testSetDurationSecondsValid() {
        Integer validDuration = 7200; // 2 hours
        video.setDurationSeconds(validDuration);
        assertEquals(validDuration, video.getDurationSeconds());
    }

    @Test
    void testSetDurationSecondsMinimum() {
        Integer minimumDuration = 1;
        assertDoesNotThrow(() -> video.setDurationSeconds(minimumDuration));
        assertEquals(minimumDuration, video.getDurationSeconds());
    }

    @Test
    void testSetDurationSecondsNull() {
        assertDoesNotThrow(() -> video.setDurationSeconds(null));
        assertNull(video.getDurationSeconds());
    }

    @Test
    void testSetDurationSecondsZero() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setDurationSeconds(0)
        );
        assertEquals("Duration must be at least 1 second", exception.getMessage());
    }

    @Test
    void testSetDurationSecondsNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setDurationSeconds(-1)
        );
        assertEquals("Duration must be at least 1 second", exception.getMessage());
    }

    @Test
    void testSetDurationSecondsLargeNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setDurationSeconds(-3600)
        );
        assertEquals("Duration must be at least 1 second", exception.getMessage());
    }

    // Platform setter tests
    @Test
    void testSetPlatformValid() {
        String validPlatform = "YouTube";
        video.setPlatform(validPlatform);
        assertEquals(validPlatform, video.getPlatform());
    }

    @Test
    void testSetPlatformNull() {
        assertDoesNotThrow(() -> video.setPlatform(null));
        assertNull(video.getPlatform());
    }

    @Test
    void testSetPlatformBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setPlatform("   ")
        );
        assertEquals("Platform cannot be blank", exception.getMessage());
    }

    @Test
    void testSetPlatformEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setPlatform("")
        );
        assertEquals("Platform cannot be blank", exception.getMessage());
    }

    // URL setter tests - Valid cases
    @Test
    void testSetUrlValidHttps() {
        String validUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        assertDoesNotThrow(() -> video.setUrl(validUrl));
        assertEquals(validUrl, video.getUrl());
    }

    @Test
    void testSetUrlValidHttp() {
        String validUrl = "http://www.example.com/video";
        assertDoesNotThrow(() -> video.setUrl(validUrl));
        assertEquals(validUrl, video.getUrl());
    }

    @Test
    void testSetUrlValidComplexUrl() {
        String complexUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ&t=30s&list=PLrAXtmRdnEQy1rHs1EB1VD5Y1y_2JJSu";
        assertDoesNotThrow(() -> video.setUrl(complexUrl));
        assertEquals(complexUrl, video.getUrl());
    }

    @Test
    void testSetUrlNull() {
        assertDoesNotThrow(() -> video.setUrl(null));
        assertNull(video.getUrl());
    }

    // URL setter tests - Invalid cases
    @Test
    void testSetUrlBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setUrl("   ")
        );
        assertEquals("URL cannot be blank", exception.getMessage());
    }

    @Test
    void testSetUrlEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setUrl("")
        );
        assertEquals("URL cannot be blank", exception.getMessage());
    }

    @Test
    void testSetUrlInvalidFormat() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setUrl("not-a-url")
        );
        assertEquals("URL must be a valid HTTP or HTTPS URL", exception.getMessage());
    }

    @Test
    void testSetUrlMissingProtocol() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setUrl("www.youtube.com/watch?v=dQw4w9WgXcQ")
        );
        assertEquals("URL must be a valid HTTP or HTTPS URL", exception.getMessage());
    }

    @Test
    void testSetUrlInvalidProtocol() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setUrl("ftp://example.com/video.mp4")
        );
        assertEquals("URL must be a valid HTTP or HTTPS URL", exception.getMessage());
    }

    @Test
    void testSetUrlWithSpaces() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setUrl("https://www.youtube.com/watch?v=dQw4w9 WgXcQ")
        );
        assertEquals("URL must be a valid HTTP or HTTPS URL", exception.getMessage());
    }

    // Release year setter tests
    @Test
    void testSetReleaseYearValid() {
        Integer validYear = 2023;
        video.setReleaseYear(validYear);
        assertEquals(validYear, video.getReleaseYear());
    }

    @Test
    void testSetReleaseYearMinimumValid() {
        Integer minimumYear = 1888; // First motion picture
        assertDoesNotThrow(() -> video.setReleaseYear(minimumYear));
        assertEquals(minimumYear, video.getReleaseYear());
    }

    @Test
    void testSetReleaseYearCurrentYear() {
        Integer currentYear = java.time.Year.now().getValue();
        assertDoesNotThrow(() -> video.setReleaseYear(currentYear));
        assertEquals(currentYear, video.getReleaseYear());
    }

    @Test
    void testSetReleaseYearFutureValid() {
        Integer futureYear = java.time.Year.now().getValue() + 5;
        assertDoesNotThrow(() -> video.setReleaseYear(futureYear));
        assertEquals(futureYear, video.getReleaseYear());
    }

    @Test
    void testSetReleaseYearNull() {
        assertDoesNotThrow(() -> video.setReleaseYear(null));
        assertNull(video.getReleaseYear());
    }

    @Test
    void testSetReleaseYearTooOld() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setReleaseYear(1887) // Before first motion picture
        );
        assertTrue(exception.getMessage().contains("Release year must be between 1888 and"));
    }

    @Test
    void testSetReleaseYearTooFuture() {
        Integer tooFutureYear = java.time.Year.now().getValue() + 15;
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setReleaseYear(tooFutureYear)
        );
        assertTrue(exception.getMessage().contains("Release year must be between 1888 and"));
    }

    @Test
    void testSetReleaseYearNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setReleaseYear(-1)
        );
        assertTrue(exception.getMessage().contains("Release year must be between 1888 and"));
    }

    @Test
    void testSetReleaseYearZero() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setReleaseYear(0)
        );
        assertTrue(exception.getMessage().contains("Release year must be between 1888 and"));
    }

    @Test
    void testSetReleaseYearVeryOld() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> video.setReleaseYear(1800)
        );
        assertTrue(exception.getMessage().contains("Release year must be between 1888 and"));
    }

    // Test toString method
    @Test
    void testToString() {
        video.setDirector("Christopher Nolan");
        video.setDurationSeconds(8580); // 2h 23m
        video.setPlatform("YouTube");
        video.setUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        video.setReleaseYear(2010);

        String result = video.toString();
        assertNotNull(result);
        assertTrue(result.contains("Video{"));
        assertTrue(result.contains("director='Christopher Nolan'"));
        assertTrue(result.contains("durationSeconds=8580"));
        assertTrue(result.contains("platform='YouTube'"));
        assertTrue(result.contains("url='https://www.youtube.com/watch?v=dQw4w9WgXcQ'"));
        assertTrue(result.contains("releaseYear=2010"));
    }

    // Integration test with all valid values
    @Test
    void testCompleteVideoSetup() {
        Video completeVideo = new Video("Inception", "Christopher Nolan");
        completeVideo.setDirector("Christopher Nolan");
        completeVideo.setDurationSeconds(8880); // 2h 28m
        completeVideo.setPlatform("Netflix");
        completeVideo.setUrl("https://www.netflix.com/title/70131314");
        completeVideo.setReleaseYear(2010);

        assertEquals("Inception", completeVideo.getTitle());
        assertEquals("Christopher Nolan", completeVideo.getAuthor());
        assertEquals("Christopher Nolan", completeVideo.getDirector());
        assertEquals(Integer.valueOf(8880), completeVideo.getDurationSeconds());
        assertEquals("Netflix", completeVideo.getPlatform());
        assertEquals("https://www.netflix.com/title/70131314", completeVideo.getUrl());
        assertEquals(Integer.valueOf(2010), completeVideo.getReleaseYear());
    }

    // Test with various platform types
    @Test
    void testVariousPlatforms() {
        String[] platforms = {"YouTube", "Netflix", "Vimeo", "Amazon Prime", "Disney+", "Hulu"};

        for (String platform : platforms) {
            assertDoesNotThrow(() -> video.setPlatform(platform));
            assertEquals(platform, video.getPlatform());
        }
    }

    // Test with various duration scenarios
    @Test
    void testVariousDurations() {
        // Very short video (1 second)
        assertDoesNotThrow(() -> video.setDurationSeconds(1));
        assertEquals(Integer.valueOf(1), video.getDurationSeconds());

        // Short video (30 seconds)
        assertDoesNotThrow(() -> video.setDurationSeconds(30));
        assertEquals(Integer.valueOf(30), video.getDurationSeconds());

        // Medium video (10 minutes)
        assertDoesNotThrow(() -> video.setDurationSeconds(600));
        assertEquals(Integer.valueOf(600), video.getDurationSeconds());

        // Long video (3 hours)
        assertDoesNotThrow(() -> video.setDurationSeconds(10800));
        assertEquals(Integer.valueOf(10800), video.getDurationSeconds());

        // Very long video (10 hours)
        assertDoesNotThrow(() -> video.setDurationSeconds(36000));
        assertEquals(Integer.valueOf(36000), video.getDurationSeconds());
    }

    // Test with various URL formats
    @Test
    void testVariousUrlFormats() {
        String[] validUrls = {
            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            "https://vimeo.com/123456789",
            "https://www.netflix.com/title/70131314",
            "https://www.twitch.tv/videos/123456789",
            "http://example.com/video.mp4",
            "https://drive.google.com/file/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/view"
        };

        for (String url : validUrls) {
            assertDoesNotThrow(() -> video.setUrl(url), "Failed for URL: " + url);
            assertEquals(url, video.getUrl());
        }
    }

    // Test historical video years
    @Test
    void testHistoricalVideoYears() {
        // Early cinema
        assertDoesNotThrow(() -> video.setReleaseYear(1888)); // First motion picture
        assertDoesNotThrow(() -> video.setReleaseYear(1895)); // Lumière Brothers
        assertDoesNotThrow(() -> video.setReleaseYear(1927)); // The Jazz Singer (first talkie)
        assertDoesNotThrow(() -> video.setReleaseYear(1939)); // The Wizard of Oz
        assertDoesNotThrow(() -> video.setReleaseYear(1977)); // Star Wars
        assertDoesNotThrow(() -> video.setReleaseYear(2009)); // Avatar
    }
}
