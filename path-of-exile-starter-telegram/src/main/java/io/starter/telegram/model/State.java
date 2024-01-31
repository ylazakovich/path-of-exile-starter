package io.starter.telegram.model;

public enum State {
  START, SKILLS, SETTINGS, FEEDBACK,
  NO_CMD, WAIT_FOR_CMD, SKILLS_WAIT_FOR_CMD,
  SKILLS_ALL, SKILLS_ANY;

  @Override
  public String toString() {
    return this.name();
  }
}
