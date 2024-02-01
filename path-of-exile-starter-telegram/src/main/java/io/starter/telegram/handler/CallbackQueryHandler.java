package io.starter.telegram.handler;

import java.util.Objects;

import io.starter.telegram.cash.CallbackCash;
import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

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

  public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
    final long chatId = buttonQuery.getMessage().getChatId();
    final long userId = buttonQuery.getFrom().getId();
    BotApiMethod<?> callBackAnswer = null;
    CallbackState state = Objects.requireNonNull(CallbackState.byData(buttonQuery.getData()));
    switch (state) {
      case SKILL_ALL:
        break;
      case SKILLS_ANY:
        break;
      default:
        callBackAnswer = new SendMessage(String.valueOf(chatId), "TODO...");
        cash.saveState(userId, CallbackState.NO_CMD);
        break;
    }
    return callBackAnswer;
  }
}
