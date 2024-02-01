package io.starter.telegram.model;

import io.starter.aggregator.model.Skill;
import lombok.experimental.UtilityClass;

@UtilityClass
public class State {

  public enum Message {
    START("/start"),
    SKILLS("Skills"),
    SETTINGS("Settings"),
    FEEDBACK("Feedback");

    public final String value;

    Message(String value) {
      this.value = value;
    }

    public static Message byText(String text) {
      for (Message state : Message.values()) {
        if (state.value.equalsIgnoreCase(text)) {
          return state;
        }
      }
      return null;
    }
  }

  public enum Callback {
    NO_CMD(""),
    SKILL_ALL("skills_all_pressed"),
    SKILLS_ANY("skills_any_pressed");

    public final String value;

    Callback(String value) {
      this.value = value;
    }

    public static Callback byData(String text) {
      for (Callback state : Callback.values()) {
        if (state.value.equalsIgnoreCase(text)) {
          return state;
        }
      }
      return null;
    }
  }
}
