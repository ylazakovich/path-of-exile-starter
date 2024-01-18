package io.starter.telegram.model;

public enum State {
  START,
  NO_CMD,
  WAIT_FOR_CMD,
  SKILLS_WAIT_FOR_CMD,
  SKILLS_ALL,
  SKILLS_ANY
}
