package org.tomo.domain.services;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomo.domain.User;
import org.tomo.domain.commands.AddCmd;
import org.tomo.domain.commands.AddCmdMessage;
import org.tomo.domain.commands.DeleteAllCmd;
import org.tomo.domain.commands.DeleteAllCmdMessage;
import org.tomo.domain.commands.ICommandMessage;
import org.tomo.domain.commands.PrintAllCmd;
import org.tomo.domain.commands.PrintAllCmdMessage;
import org.tomo.indra.fifo.api.IndraFifo;

/**
 * Consume and process message, or all messages from {@link IndraFifo}.
 */
public class ConsumerService {
  private final Logger log = LoggerFactory.getLogger(ConsumerService.class);

  @NonNull
  private final IndraFifo<ICommandMessage> indraFifo;
  @NonNull
  private final AddCmd addCmd;
  @NonNull
  private final DeleteAllCmd deleteAllCmd;
  @NonNull
  private final PrintAllCmd printAllCmd;

  /**
   * Constructor.
   */
  public ConsumerService(IndraFifo<ICommandMessage> indraFifo,
                         AddCmd addCmd,
                         DeleteAllCmd deleteAllCmd,
                         PrintAllCmd printAllCmd) {
    this.indraFifo = indraFifo;
    this.addCmd = addCmd;
    this.deleteAllCmd = deleteAllCmd;
    this.printAllCmd = printAllCmd;
  }

  /**
   * Consume and process all messages from {@link IndraFifo} in never-ending blocking loop.
   */
  @SuppressWarnings("java:S2189")
  public void consumeAll() {
    while (true) {
      try {
        ICommandMessage commandMessage = indraFifo.take();
        consumeMessage(commandMessage);
      } catch (InterruptedException e) {
        log.info("FIFO CONSUMER thread ended during waiting for some message. Fifo was empty.");
        Thread.currentThread().interrupt();
      }
    }
  }

  /**
   * Consume and process just one command's message.
   */
  public void consumeMessage(ICommandMessage commandMessage) {
    if (commandMessage instanceof AddCmdMessage addCommandMessage) {
      log.info("CONSUMER: Processing AddCmdMessage");

      User user = addCommandMessage.getUser();
      addCmd.add(user);
    } else if (commandMessage instanceof DeleteAllCmdMessage) {
      log.info("CONSUMER: Processing DeleteAllCmd");
      deleteAllCmd.deleteAll();
    } else if (commandMessage instanceof PrintAllCmdMessage) {
      log.info("CONSUMER: Processing PrintAllCmd");
      printAllCmd.printAll();
    }
  }
}
