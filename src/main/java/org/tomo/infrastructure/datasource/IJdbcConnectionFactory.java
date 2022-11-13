package org.tomo.infrastructure.datasource;

import java.sql.Connection;

/**
 * Connection factory for the database.
 *
 * <ul>
 * <li>Thread-safe.</li>
 * <li>Returns JDBC database {@link Connection}.</li>
 * </ul>
 *
 * <p>It depends on the implementation if it is connection from database pool, or single connection.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public interface IJdbcConnectionFactory {
  Connection getConnection();
}
