package org.tomo;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomo.infrastructure.datasource.SqlDataSource;
import org.tomo.shared.exceptions.AppInitializationException;

/**
 * Initialization routines for starting application.
 *
 * <p>Call {@link #init()} for complete initialization.
 */
public class AppInitialization {
  private static final Logger log = LoggerFactory.getLogger(AppInitialization.class);

  private static Dotenv dotenv;

  private AppInitialization() {
  }

  public static Dotenv getDotenv() {
    return dotenv;
  }

  /**
   * Complete application initialization.
   *
   * <p>You don't need to call another initialization routines if you use this method.
   */
  public static void init() throws AppInitializationException {
    log.info("Application initialization started ...");

    initializeEnvVariables();

    try {
      log.info("Initializing database connection pool.");
      initializeDatabase(AppInitialization.getDotenv());
    } catch (Exception e) {
      log.error("Error during application initialization.");
      throw new AppInitializationException("Error during application initialization.");
    }

    log.info("Application initialization done.");
  }

  /**
   * Read the {@code .env} file and create {@link #dotenv} instance
   * for use with {@link #getDotenv()}.
   */
  public static void initializeEnvVariables() {
    log.info("Loading environment variables.");
    AppInitialization.dotenv = Dotenv.load();
  }

  /**
   * Initialize database connection pool.
   */
  public static void initializeDatabase(@NonNull Dotenv dotenv) {
    SqlDataSource.SqlDataSourceBuilder sqlDataSourceBuilder =
        new SqlDataSource.SqlDataSourceBuilder();

    sqlDataSourceBuilder.setJdbcUrl(dotenv.get("DB_JDBC_URL"));
    sqlDataSourceBuilder.setSchema(dotenv.get("DB_SCHEMA_NAME"));
    sqlDataSourceBuilder.setUsername(dotenv.get("DB_USERNAME"));
    sqlDataSourceBuilder.setPassword(dotenv.get("DB_PASSWORD"));
    sqlDataSourceBuilder.setHikariPropertiesFile(dotenv.get("HIKARI_PROPERTIES_FILE"));

    sqlDataSourceBuilder.build();
  }
}
