package io.starter.telegram.cash.state;

public enum CallbackState {
  NO_CMD(""),
  SKILL_ALL("skills_all_pressed"),
  SKILLS_ANY("skills_any_pressed");

  public final String value;

  CallbackState(String value) {
    this.value = value;
  }

  public static CallbackState byData(String text) {
    for (CallbackState state : CallbackState.values()) {
      if (state.value.equalsIgnoreCase(text)) {
        return state;
      }
    }
    return null;
  }
}
