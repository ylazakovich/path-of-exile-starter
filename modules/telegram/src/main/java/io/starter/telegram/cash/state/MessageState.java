package io.starter.telegram.cash.state;

import io.starter.telegram.config.Emoji;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MessageState {

  FIRST_START("/start"),
  START("%s Start".formatted(Emoji.YELLOW_RIBBON)),
  SETTINGS("%s Settings".formatted(Emoji.GEAR)),
  CLEAN("%s Clean".formatted(Emoji.GAME_DIE)),
  FEEDBACK("%s Feedback".formatted(Emoji.PERFORMING_ARTS));

  public final String value;

  public static MessageState byText(String text) {
    for (MessageState state : MessageState.values()) {
      if (state.value.equalsIgnoreCase(text)) {
        return state;
      }
    }
    return FIRST_START;
  }
}
