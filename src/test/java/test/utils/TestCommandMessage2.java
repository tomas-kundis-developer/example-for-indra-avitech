package test.utils;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.tomo.domain.commands.ICommandMessage;

/**
 * Command message just for testing purposes.
 *
 * <p>To make a unit test autonomous,
 * it's better to not depends on classes outside the tested unit environment.
 *
 * <p>This command message has id attribute to distinguish a concrete commands instances.
 */
@RequiredArgsConstructor
public class TestCommandMessage2 implements ICommandMessage {
  @Getter
  @NonNull
  private final String id;

  @Override
  public String toString() {
    return this.getId();
  }
}
