package io.starter.telegram.cash.state;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
public enum CallbackState {

  NO_CMD(StringUtils.EMPTY),
  SETTINGS("Pressing button with 'Settings'"),
  SETTING_STANDARD("Pressing button with 'Standard'"),
  SETTING_LEAGUE("Pressing button with 'League'"),
  SETTING_HARDCORE("Pressing button with 'Hardcore'"),
  SETTING_LEAGUE_HARDCORE("Pressing button with 'League Hardcore'"),
  SKILLS("Pressing button with 'Skills'"),
  ALL_SKILLS("Pressing button with 'All skills'"),
  REFRESH_SKILLS("Pressing button with 'Refresh' in Menu with 'Skills'"),
  ITEMS_AFTER_BLESSING("Pressing button with 'Blessing Items'"),
  CURRENT("Pressing on current page"),
  SKILLS_NEXT("skills - Pressing NEXT"),
  SKILLS_PREVIOUS("skills - Pressing PREVIOUS");

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
