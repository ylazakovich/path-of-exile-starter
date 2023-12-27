package io.automation.ex;

public class GemNotFoundException extends RuntimeException{

  public GemNotFoundException(String message) {
    super(message);
  }
}
