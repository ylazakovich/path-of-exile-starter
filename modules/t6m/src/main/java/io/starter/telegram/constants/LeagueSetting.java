package io.starter.telegram.constants;

import io.starter.telegram.cash.state.CallbackState;

public enum LeagueSetting {

  STANDARD(1L, CallbackState.SETTING_STANDARD),
  LEAGUE(9L, CallbackState.SETTING_LEAGUE),
  HARDCORE(2L, CallbackState.SETTING_HARDCORE),
  LEAGUE_HARDCORE(10L ,CallbackState.SETTING_LEAGUE_HARDCORE);

  public long id;
  public CallbackState state;

  LeagueSetting(Long id, CallbackState state) {
    this.id = id;
    this.state = state;
  }

  public static LeagueSetting byCallbackState(CallbackState state) {
    for (LeagueSetting leagueSetting : LeagueSetting.values()) {
      if (leagueSetting.state.equals(state)) {
        return leagueSetting;
      }
    }
    return null;
  }
}
