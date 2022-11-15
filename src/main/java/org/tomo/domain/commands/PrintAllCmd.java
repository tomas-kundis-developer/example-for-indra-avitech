package org.tomo.domain.commands;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomo.domain.IUserRepository;
import org.tomo.domain.User;
import org.tomo.infrastructure.DataAccessException;

/**
 * Print all users from susers db table.
 */
public class PrintAllCmd implements ICommand {
  private static final String ID = "PrintAll";

  private final Logger log = LoggerFactory.getLogger(PrintAllCmd.class);

  IUserRepository userRepo;

  public PrintAllCmd(IUserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public String getId() {
    return ID;
  }

  @SuppressWarnings("java:S106")
  public void printAll() {
    try {
      userRepo.findAll().forEach(user -> System.out.println(formatUser(user)));
    } catch (DataAccessException e) {
      log.error("Can't fetch all users from database.");
    }
  }

  private String formatUser(@NonNull User user) {
    return "#"
        + user.getId()
        + ", '"
        + user.getGuid()
        + "', "
        + user.getName();
  }
}
