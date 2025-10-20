package com.columbia.coms4156.citationservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Utility class to check database connectivity at application startup.
 */
@Component
public class DatabaseStartupCheck implements CommandLineRunner {

  private final DataSource dataSource;

  public DatabaseStartupCheck(DataSource dataSource) {
    this.dataSource = dataSource;
  }

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