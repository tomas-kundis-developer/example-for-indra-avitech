package org.tomo.domain.commands;

import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import org.tomo.domain.User;

/**
 * Message for {@link AddCmd} command.
 */
@Data
public class AddCmdMessage implements ICommandMessage, Serializable {
  @Serial
  private static final long serialVersionUID = 4286908077777013100L;

  @NonNull
  @Setter(AccessLevel.NONE)
  private User user;

  private AddCmdMessage(@NonNull final User user) {
    this.user = new User(user);
  }

  public static AddCmdMessage create(@NonNull final User user) {
    return new AddCmdMessage(user);
  }

  @Override
  public String getId() {
    return "AddCmdMessage";
  }
}
