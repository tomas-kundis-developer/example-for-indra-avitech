package org.tomo.domain.commands;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomo.domain.IUserRepository;
import org.tomo.domain.User;
import org.tomo.infrastructure.DataAccessException;

/**
 * Add the user to susers db table.
 */
public class AddCmd implements ICommand {
  private static final String ID = "Add";

  private final Logger log = LoggerFactory.getLogger(AddCmd.class);

  IUserRepository userRepo;

  public AddCmd(IUserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public void add(@NonNull User user) {
    try {
      userRepo.add(user);
      log.info("Added user: {}", user);
    } catch (DataAccessException e) {
      log.error("User not added: {}", user);
    }
  }

  @Override
  public String getId() {
    return ID;
  }
}
