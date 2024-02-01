package io.starter.telegram.handler;

import io.starter.telegram.cash.BotCallbackStateCash;
import io.starter.telegram.model.State;
import io.starter.telegram.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class CallbackQueryHandler {

  private final BotCallbackStateCash cash;
  private final MenuService menuService;

  @Autowired
  public CallbackQueryHandler(BotCallbackStateCash cash,
                              MenuService menuService) {
    this.cash = cash;
    this.menuService = menuService;
  }

  public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
    final long chatId = buttonQuery.getMessage().getChatId();
    final long userId = buttonQuery.getFrom().getId();
    BotApiMethod<?> callBackAnswer = null;
    State.Callback state = State.Callback.byData(buttonQuery.getData());
    switch (state) {
      case SKILL_ALL:
        break;
      case SKILLS_ANY:
        break;
      default:
        callBackAnswer = new SendMessage(String.valueOf(chatId), "TODO...");
        cash.saveState(userId, State.Callback.NO_CMD);
        break;
    }
    return callBackAnswer;
  }
}
