package com.inomera.adapter.config.bridge.exception;

public class AdapterConfigException extends RuntimeException {

  public AdapterConfigException() {
  }

  public AdapterConfigException(String message) {
    super(message);
  }

  public AdapterConfigException(String message, Throwable cause) {
    super(message, cause);
  }
}
