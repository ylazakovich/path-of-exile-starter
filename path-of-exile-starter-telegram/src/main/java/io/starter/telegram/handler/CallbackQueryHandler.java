package io.starter.telegram.handler;

import io.starter.telegram.cash.CallbackCash;
import io.starter.telegram.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CallbackQueryHandler {

  private final CallbackCash cash;
  private final MenuService menuService;

  @Autowired
  public CallbackQueryHandler(CallbackCash cash,
                              MenuService menuService) {
    this.cash = cash;
    this.menuService = menuService;
  }
}
