package org.tomo.infrastructure.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomo.domain.IUserRepository;
import org.tomo.domain.User;
import org.tomo.infrastructure.DataAccessException;
import org.tomo.infrastructure.datasource.IJdbcConnectionFactory;
import org.tomo.infrastructure.datasource.SqlDataSource;

/**
 * JDBC implementation of {@link IUserRepository}.
 */
public class UserJdbcRepository implements IUserRepository {
  private static final Logger logClass = LoggerFactory.getLogger(UserJdbcRepository.class);

  private static UserJdbcRepository instance;

  private final Logger log = LoggerFactory.getLogger(UserJdbcRepository.class);


  private final IJdbcConnectionFactory dataSource = SqlDataSource.getInstance();

  private UserJdbcRepository() {
  }

  /**
   * Get singleton instance.
   */
  public static synchronized UserJdbcRepository getInstance() {
    if (instance == null) {
      logClass.debug("Singleton instance created.");
      instance = new UserJdbcRepository();
    }
    return instance;
  }

  @Override
  public void add(@NonNull User user) {
    String queryStr = "INSERT INTO susers (id, guid, name) VALUES (?, ?, ?)";
    Connection connection = dataSource.getConnection();

    try (PreparedStatement preparedStmt = connection.prepareStatement(queryStr)) {
      preparedStmt.setLong(1, user.getId());
      preparedStmt.setString(2, user.getGuid());
      preparedStmt.setString(3, user.getName());
      preparedStmt.executeUpdate();
    } catch (SQLException e) {
      log.error("Error saving a new user into database!", e);
      throw new DataAccessException();
    } finally {
      closeConnection(connection);
    }
  }

  @Override
  public User addNewGenerateId(@NonNull User user) {
    String queryStr = "INSERT INTO susers (guid, name) VALUES (?, ?)";
    Connection connection = dataSource.getConnection();

    try (PreparedStatement preparedStmt =
             connection.prepareStatement(queryStr, Statement.RETURN_GENERATED_KEYS)) {
      preparedStmt.setString(1, user.getGuid());
      preparedStmt.setString(2, user.getName());
      preparedStmt.executeUpdate();

      ResultSet generatedKeys = preparedStmt.getGeneratedKeys();

      if (generatedKeys.next()) {
        long generatedId = generatedKeys.getLong(1);
        log.info("User added: id: #{}", generatedId);
        return new User(generatedId, user.getGuid(), user.getName());
      } else {
        log.error("Error saving a new user into database - can't obtain generated id!");
        throw new DataAccessException();
      }
    } catch (SQLException e) {
      log.error("Error saving a new user into database!", e);
      throw new DataAccessException();
    } finally {
      closeConnection(connection);
    }
  }

  @Override
  public Iterable<User> findAll() {
    String queryStr = "SELECT * FROM susers";
    Connection connection = dataSource.getConnection();
    List<User> users = new ArrayList<>();

    try (PreparedStatement preparedStmt = connection.prepareStatement(queryStr)) {
      ResultSet rs = preparedStmt.executeQuery();
      log.info("Query executed: SELECT all users.");

      while (rs.next()) {
        users.add(mapRowToUser(rs));
        log.info("Iterating next user.");
      }
    } catch (SQLException e) {
      log.error("Error selecting all users from database!", e);
      throw new DataAccessException();
    } finally {
      closeConnection(connection);
    }

    return users;
  }

  @Override
  public void deleteAll() {
    String queryStr = "DELETE FROM susers";
    Connection connection = dataSource.getConnection();

    try (PreparedStatement preparedStmt = connection.prepareStatement(queryStr)) {
      preparedStmt.executeUpdate();
      log.info("Query executed: DELETE all users.");
    } catch (SQLException e) {
      log.error("Error deleting all users from database!", e);
      throw new DataAccessException();
    } finally {
      closeConnection(connection);
    }
  }

  private void closeConnection(Connection connection) {
    try {
      connection.close();
      log.info("Connection closed.");
    } catch (SQLException e) {
      log.error("Error closing db connection!", e);
      throw new DataAccessException();
    }
  }

  private User mapRowToUser(ResultSet row) throws SQLException {
    User user = new User(row.getLong("id"));
    user.setGuid(row.getString("guid"));
    user.setName(row.getString("name"));
    return user;
  }
}
