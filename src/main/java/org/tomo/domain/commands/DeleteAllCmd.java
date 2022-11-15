package org.tomo.domain.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomo.domain.IUserRepository;
import org.tomo.infrastructure.DataAccessException;

/**
 * Delete all users from susers db table.
 */
public class DeleteAllCmd implements ICommand {
  private static final String ID = "PrintAll";

  private final Logger log = LoggerFactory.getLogger(DeleteAllCmd.class);

  IUserRepository userRepo;

  public DeleteAllCmd(IUserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public String getId() {
    return ID;
  }

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  public void deleteAll() {
    try {
      userRepo.deleteAll();
      log.info("Users deleted.");
    } catch (DataAccessException e) {
      log.error("Users not deleted.");
    }
  }
}
