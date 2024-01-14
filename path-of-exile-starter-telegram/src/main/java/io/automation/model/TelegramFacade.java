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
      CallbackQuery callbackQuery = update.getCallbackQuery();
      return callbackQueryHandler.processCallbackQuery(callbackQuery);
    } else {
      Message message = update.getMessage();
      if (message != null && message.hasText()) {
        return handleInputMessage(message);
      }
    }
    return null;
  }

  private BotApiMethod<?> handleInputMessage(Message message) {
    State state = State.NO_COMMAND;
    String inputMsg = message.getText();
    switch (inputMsg) {
      case "/start":
        state = State.START;
        botStateCash.saveBotState(message.getFrom().getId(), State.START);
        break;
      case "":
        break;
      default:
        state = botStateCash.getBotStateMap().get(message.getFrom().getId()) == null
            ? State.START
            : botStateCash.getBotStateMap().get(message.getFrom().getId());
    }
    return messageHandler.handle(message, state);
  }
}