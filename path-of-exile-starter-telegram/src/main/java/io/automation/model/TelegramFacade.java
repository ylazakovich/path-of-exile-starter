package io.automation.model;

import io.automation.cash.BotStateCash;
import io.automation.model.handler.CallbackQueryHandler;
import io.automation.model.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramFacade {

  private final MessageHandler messageHandler;
  private final CallbackQueryHandler callbackQueryHandler;
  private final BotStateCash botStateCash;

  public TelegramFacade(MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler,
                        BotStateCash botStateCash) {
    this.messageHandler = messageHandler;
    this.callbackQueryHandler = callbackQueryHandler;
    this.botStateCash = botStateCash;
  }

  public BotApiMethod<?> handleUpdate(Update update) {
    if (update.hasCallbackQuery()) {
      log.info("Received callback");
      // TODO: need to have a look at guide about callbacks
      CallbackQuery callbackQuery = update.getCallbackQuery();
      return callbackQueryHandler.processCallbackQuery(callbackQuery);
    } else {
      log.info("Received message");
      Message message = update.getMessage();
      if (message != null && message.hasText()) {
        return handleInputMessage(message);
      }
    }
    return null;
  }

  private BotApiMethod<?> handleInputMessage(Message message) {
    switch (message.getText()) {
      case "/start":
        botStateCash.saveState(message.getFrom().getId(), State.START);
        break;
      case "Skills":
        botStateCash.saveState(message.getFrom().getId(), State.SKILLS_WAIT_EVENT);
        break;
      case "ALL":
        botStateCash.saveState(message.getFrom().getId(), State.SKILLS_ALL_EVENT);
        break;
      case "ANY":
        botStateCash.saveState(message.getFrom().getId(), State.SKILLS_ANY_EVENT);
        break;
    }
    return messageHandler.handle(message, botStateCash.getCurrentState(message.getFrom()));
  }
}
