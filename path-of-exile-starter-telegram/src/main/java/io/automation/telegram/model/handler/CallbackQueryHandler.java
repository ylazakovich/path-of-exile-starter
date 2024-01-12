package io.automation.telegram.model.handler;

import io.automation.telegram.cash.BotStateCash;
import io.automation.telegram.cash.EventCash;
import io.automation.telegram.entity.Event;
import io.automation.telegram.model.EventFreq;
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
  private final EventCash eventCash;
  private final MenuService menuService;
  private final EventHandler eventHandler;

  @Autowired
  public CallbackQueryHandler(BotStateCash botStateCash, EventCash eventCash, MenuService menuService,
                              EventHandler eventHandler) {
    this.botStateCash = botStateCash;
    this.eventCash = eventCash;
    this.menuService = menuService;
    this.eventHandler = eventHandler;
  }

  public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
    final long chatId = buttonQuery.getMessage().getChatId();
    final long userId = buttonQuery.getFrom().getId();
    BotApiMethod<?> callBackAnswer = null;
    String data = buttonQuery.getData();
    switch (data) {
      case "Skills":
        callBackAnswer = new SendMessage(String.valueOf(chatId), "Введите номер напоминания из списка.");
        botStateCash.saveBotState(userId, State.ENTER_NUMBER_EVENT);
        break;
      case "TODO":
        break;
    }
    return callBackAnswer;
  }
}
