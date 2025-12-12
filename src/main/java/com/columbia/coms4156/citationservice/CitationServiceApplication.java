package com.columbia.coms4156.citationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Citation Service API.
 * This Spring Boot application provides comprehensive citation creation and management
 * functionality for academic and professional use.
 *
 * <p>The Citation Service currently supports book citations in MLA format, with planned
 * expansion to include websites, films, and other source types. The service provides
 * both storage capabilities for citation libraries and on-demand citation generation.</p>
 *
 * <p>Key features include:</p>
 * <ul>
 *   <li>RESTful API for citation management</li>
 *   <li>MLA format citation generation</li>
 *   <li>Persistent storage of citation sources</li>
 *   <li>CRUD operations for citation libraries</li>
 * </ul>
 *
 * @author Citation Service Team
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
@SuppressWarnings("PMD.UseUtilityClass")
public class CitationServiceApplication {



  /**
   * Main method to start the Spring Boot Citation Service application.
   * Initializes the embedded Tomcat server and loads the application context
   * with all necessary beans and configurations.
   *
   * <p>The application will start on port 8080 by default and provide:</p>
   * <ul>
   *   <li>REST API endpoints at {@code http://localhost:8080/api/}</li>
   *   <li>H2 database console at {@code http://localhost:8080/h2-console}</li>
   * </ul>
   *
   * @param args Command line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(CitationServiceApplication.class, args);
  }
}