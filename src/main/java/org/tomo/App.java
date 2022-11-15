package org.tomo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomo.shared.exceptions.AppInitializationException;

/**
 * Main application class.
 */
public class App {
  private static final Logger log = LoggerFactory.getLogger(App.class);

  /**
   * Application start.
   */
  public static void main(String[] args) {
    log.info("Application started.");

    try {
      AppInitialization.init();
    } catch (AppInitializationException e) {
      log.error("Exit with error code 1");
      System.exit(1);
    }
  }
}
