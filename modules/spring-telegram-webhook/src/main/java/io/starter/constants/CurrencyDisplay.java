package io.starter.constants;

import io.starter.cache.state.CallbackState;

public enum CurrencyDisplay {
  CHAOS(CallbackState.SETTING_CURRENCY_CHAOS),
  DIVINE(CallbackState.SETTING_CURRENCY_DIVINE);

  public final CallbackState callbackState;

  CurrencyDisplay(CallbackState callbackState) {
    this.callbackState = callbackState;
  }

  public static CurrencyDisplay byCallbackState(CallbackState state) {
    for (CurrencyDisplay currencyDisplay : CurrencyDisplay.values()) {
      if (currencyDisplay.callbackState == state) {
        return currencyDisplay;
      }
    }
    return null;
  }
}
