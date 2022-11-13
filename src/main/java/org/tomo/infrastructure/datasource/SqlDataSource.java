package org.tomo.infrastructure.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomo.infrastructure.DataAccessException;

/**
 * Returns database {@link Connection} from the connection pool.
 *
 * <p>HikariCP is used as connection pool library.
 *
 * <p>Singleton, thread-safe service.
 *
 * <p>This singleton class must be first configured and then instantiated
 * with {@link SqlDataSourceBuilder}.
 *
 * @see IJdbcConnectionFactory
 */
public class SqlDataSource implements IJdbcConnectionFactory {
  private final Logger log = LoggerFactory.getLogger(SqlDataSource.class);

  private static SqlDataSource instance;

  private final HikariDataSource hikariDsPool;

  private SqlDataSource(SqlDataSourceBuilder builder) {
    this.hikariDsPool = createHikariDataSourcePool(builder);
  }

  private HikariDataSource createHikariDataSourcePool(SqlDataSourceBuilder builder) {
    HikariConfig config = new HikariConfig(builder.getHikariPropertiesFile());

    config.setJdbcUrl(builder.getJdbcUrl());
    config.setSchema(builder.getSchema());
    config.setUsername(builder.getUsername());
    config.setPassword(builder.getPassword());

    return new HikariDataSource(config);
  }

  /**
   * Returns database {@link Connection} from connection pool.
   *
   * <p>If there is no available database connection in the pool, the calling thread will be blocked
   * until some connection to become available or until defined timeout runs out.
   *
   * <p>If timeout is reached, the exception is thrown.
   */
  public Connection getConnection() {
    try {
      return this.hikariDsPool.getConnection();
    } catch (SQLException e) {
      log.error("Can't get connection from the pool!", e);
      throw new DataAccessException();
    }
  }

  /**
   * Returns singleton instance.
   */
  public static SqlDataSource getInstance() {
    if (instance == null) {
      throw new UnsupportedOperationException("""
          SqlDataSource is not instantiated.
           Use SqlDataSourceBuilder to instantiate this singleton.""");
    }

    return instance;
  }

  public static synchronized void shutdown() {
    instance = null;
  }

  /**
   * Builder for {@link SqlDataSource} service.
   *
   * <p>All setters are mandatory (even it's empty string such an empty password for example).
   *
   * <p>Not thread-safe, but instantiated {@link SqlDataSource} is thread-safe.
   */
  @Data
  @NoArgsConstructor
  public static class SqlDataSourceBuilder {
    private final Logger log = LoggerFactory.getLogger(SqlDataSourceBuilder.class);

    @NonNull
    private String jdbcUrl;

    @NonNull
    private String schema;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String hikariPropertiesFile;

    // TODO 2022-11-13 TOKU: Fix SonarLint: Instance methods should not write to "static" fields.
    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public void build() {
      if (instance != null) {
        log.error("Can't build SqlDataSource singleton because it is always instantiated.");
        throw new UnsupportedOperationException(
            "Can't build SqlDataSource singleton because it is always instantiated.");
      }

      Objects.requireNonNull(this.jdbcUrl);
      Objects.requireNonNull(this.schema);
      Objects.requireNonNull(this.username);
      Objects.requireNonNull(this.password);
      Objects.requireNonNull(this.hikariPropertiesFile);

      SqlDataSource.instance = new SqlDataSource(this);
    }
  }
}
