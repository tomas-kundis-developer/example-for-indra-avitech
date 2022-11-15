package org.tomo.shared.exceptions;

/**
 * Use for early detection of initialization problem for terminating application start
 * as soon as possible.
 */
public class AppInitializationException extends Exception {
  public AppInitializationException(String message) {
    super(message);
  }
}
