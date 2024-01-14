package io.automation.model.handler;

import io.automation.service.MenuService;
import io.automation.cash.BotStateCash;
import io.automation.model.State;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class MessageHandler {

  private final MenuService menuService;
  private final BotStateCash botStateCash;

  public MessageHandler(MenuService menuService,
                        BotStateCash botStateCash) {
    this.menuService = menuService;
    this.botStateCash = botStateCash;
  }

  public BotApiMethod<?> handle(Message message, State state) {
    log.info("Handling message");
    long userId = message.getFrom().getId();
    long chatId = message.getChatId();
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(String.valueOf(chatId));
    botStateCash.saveBotState(userId, state);
    switch (state) {
      case START:
        return menuService.getMainMenuMessage(message.getChatId(), "Select COMMAND from MENU", userId);
      default:
        throw new IllegalStateException("Unexpected value: " + state);
    }
  }
}
