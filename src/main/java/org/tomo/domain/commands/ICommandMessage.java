package org.tomo.domain.commands;

/**
 * Message for transferring commands among subsystems.
 *
 * <p>Implemented command's messages should be serializable.
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public interface ICommandMessage {

  String getId();
}