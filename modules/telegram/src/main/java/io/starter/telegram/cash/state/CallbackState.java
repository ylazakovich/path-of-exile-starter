package io.starter.telegram.cash.state;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public enum CallbackState {
  NO_CMD(StringUtils.EMPTY),
  SKILLS("Pressing button with 'Skills'"),
  ALL_SKILLS("Pressing button with 'All skills'"),
  REFRESH_SKILLS("Pressing button with 'Refresh' in Menu with 'Skills'"),
  ITEMS_AFTER_BLESSING("Pressing button with 'Blessing Items'");

  public final String value;

  public static CallbackState byData(String text) {
    for (CallbackState state : CallbackState.values()) {
      if (state.value.equalsIgnoreCase(text)) {
        return state;
      }
    }
    return NO_CMD;
  }
}
