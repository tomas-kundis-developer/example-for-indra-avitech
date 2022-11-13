package org.tomo.domain;

import lombok.NonNull;

/**
 * Repository for {@link User} domain entity.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public interface IUserRepository {

  void add(@NonNull User user);

  /**
   * Add new user and assign him a new generated id.
   *
   * <p>User's id {@link User#getId()} will be ignored.
   * The data store will generate a new itself. If id field in domain model is always mandatory,
   * you can set id to an arbitrary value.<br/>
   * For unification, use only one forgotten-id value, like 99999L for example.
   */
  User addNewGenerateId(@NonNull User entity);

  void deleteAll();

  Iterable<User> findAll();
}