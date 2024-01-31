package io.starter.telegram.model;

public enum State {
  START("/start"),
  SKILLS("Skills"),
  SETTINGS("Settings"),
  FEEDBACK("Feedback"),
  NO_CMD(""),
  WAIT_FOR_CMD(""),
  SKILLS_WAIT_FOR_CMD(""),
  SKILLS_ALL(""),
  SKILLS_ANY("");

  public final String value;

  State(String value) {
    this.value = value;
  }

  public static State byText(String text) {
    for (State state : State.values()) {
      if (state.value.equalsIgnoreCase(text)) {
        return state;
      }
    }
    return null;
  }
}
