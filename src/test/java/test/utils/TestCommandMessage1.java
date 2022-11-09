package test.utils;

import org.tomo.domain.commands.ICommandMessage;

/**
 * Command message just for testing purposes.
 *
 * <p>To make a unit test autonomous,
 * it's better to not depends on classes outside the tested unit environment.
 *
 * <p>This command message has id attribute to distinguish a concrete commands instances.
 */
public class TestCommandMessage1 implements ICommandMessage {
  @Override
  public String getId() {
    return "Test command message 1";
  }

  @Override
  public String toString() {
    return getId();
  }
}
