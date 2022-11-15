package org.tomo.domain.commands;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * Message for {@link DeleteAllCmd} command.
 */
@Data
public class DeleteAllCmdMessage implements ICommandMessage, Serializable {
  @Serial
  private static final long serialVersionUID = -913252648915421509L;

  private DeleteAllCmdMessage() {
  }

  public static DeleteAllCmdMessage create() {
    return new DeleteAllCmdMessage();
  }

  @Override
  public String getId() {
    return "DeleteAllCmdMessage";
  }
}
