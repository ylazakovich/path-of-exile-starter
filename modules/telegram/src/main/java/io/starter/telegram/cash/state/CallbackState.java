package io.starter.telegram.cash.state;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public enum CallbackState {
  NO_CMD(StringUtils.EMPTY),
  SKILLS("Pressing button with Skills"),
  BLESSING_ITEMS("Pressing button with Blessing Items"),
  SKILLS_ALL("Pressing button with ALL SKILLS"),
  SKILLS_ANY("Pressing button with ANY SKILLS");

  public final String value;

  public static CallbackState byData(String text) {
    for (CallbackState state : CallbackState.values()) {
      if (state.value.equalsIgnoreCase(text)) {
        return state;
      }
    }
    return null;
  }
}
