package io.automation.telegram.model.handler;

import io.automation.telegram.cash.BotStateCash;
import io.automation.telegram.model.State;
import io.automation.telegram.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class CallbackQueryHandler {

  private final BotStateCash botStateCash;
  private final MenuService menuService;

  @Autowired
  public CallbackQueryHandler(BotStateCash botStateCash,
                              MenuService menuService) {
    this.botStateCash = botStateCash;
    this.menuService = menuService;
  }

  public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
    final long chatId = buttonQuery.getMessage().getChatId();
    final long userId = buttonQuery.getFrom().getId();
    BotApiMethod<?> callBackAnswer = null;
    String data = buttonQuery.getData();
    switch (data) {
      case "Skills":
        callBackAnswer = new SendMessage(String.valueOf(chatId), "TODO...");
        botStateCash.saveBotState(userId, State.SKILL_EVENT);
        break;
      case "TODO":
        break;
    }
    return callBackAnswer;
  }
}
