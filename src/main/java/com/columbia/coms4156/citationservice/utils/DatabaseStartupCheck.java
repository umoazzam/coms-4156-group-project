package com.columbia.coms4156.citationservice.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Utility class to check database connectivity at application startup.
 * This class implements CommandLineRunner to execute database connectivity
 * checks when the Spring Boot application starts.
 *
 * This class was AI-generated for utility.
 */
@Component
public final class DatabaseStartupCheck implements CommandLineRunner {

  /**
   * The data source used for database connectivity checks.
   */
  private final DataSource dataSource;

  /**
   * Constructs a new DatabaseStartupCheck with the specified data source.
   *
   * @param dataSourceParam the data source to use for connectivity checks
   */
  public DatabaseStartupCheck(DataSource dataSourceParam) {
    this.dataSource = dataSourceParam;
  }

  /**
   * Executes the database connectivity check at application startup.
   * This method is called automatically by Spring Boot after the application
   * context is initialized. It attempts to establish a connection to the
   * database and logs the connection details or any errors that occur.
   *
   * @param args command line arguments (not used in this implementation)
   * @throws Exception if there are any issues during the database check
   */
  @Override
  public void run(String... args) throws Exception {
    try (Connection conn = dataSource.getConnection()) {
      System.out.println("✅ Successfully connected to database: " + conn.getMetaData().getURL());
      System.out.println("   User: " + conn.getMetaData().getUserName());
      System.out.println("   Driver: " + conn.getMetaData().getDriverName());
    } catch (Exception e) {
      System.err.println("❌ Database connection failed: " + e.getMessage());
      e.printStackTrace();
    }
  }
}