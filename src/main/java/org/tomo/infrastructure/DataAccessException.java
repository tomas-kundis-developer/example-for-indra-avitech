package org.tomo.infrastructure;

/**
 * Runtime (non-checked) exception throw when Java's {@link java.sql.SQLException} occurs.
 *
 * <ul>
 *   <li>Avoids handling low-level SQL problems in upper application layers,
 *   other then persistence layer.</li>
 *   <li>Unlike {@link java.sql.SQLException}, this is unchecked exception,
 *     you don't have to fix it/handle at every SQL step/operations that occurs.
 *     <ul>
 *       <li>Most of {@link java.sql.SQLException} occurrences are unrecoverable
 *       during the application runtime and leads to common database read/write error.</li>
 *     </ul>
 * </ul>
 */
public class DataAccessException extends RuntimeException {
}
