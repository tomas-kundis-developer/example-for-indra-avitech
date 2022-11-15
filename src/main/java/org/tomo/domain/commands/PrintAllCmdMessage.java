package org.tomo.domain.commands;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * Message for {@link PrintAllCmd} command.
 */
@Data
public class PrintAllCmdMessage implements ICommandMessage, Serializable {
  @Serial
  private static final long serialVersionUID = 635452639658240267L;

  private PrintAllCmdMessage() {
  }

  public static PrintAllCmdMessage create() {
    return new PrintAllCmdMessage();
  }

  @Override
  public String getId() {
    return "PrintAllCmdMessage";
  }
}
