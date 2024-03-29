package io.starter.telegram.cash.state;

public enum MessageState {

  START("/start"),
  SKILLS("Skills"),
  SETTINGS("Settings"),
  FEEDBACK("Feedback");

  public final String value;

  MessageState(String value) {
    this.value = value;
  }

  public static MessageState byText(String text) {
    for (MessageState state : MessageState.values()) {
      if (state.value.equalsIgnoreCase(text)) {
        return state;
      }
    }
    return null;
  }
}
