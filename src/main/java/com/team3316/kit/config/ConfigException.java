package com.team3316.kit.config;

/**
 * Exception that is raised when a certain key is not found in config
 */
public class ConfigException extends Exception {
  private static final long serialVersionUID = -658181374612523772L;

  public ConfigException (String key) {
    super(key);
  }
}
