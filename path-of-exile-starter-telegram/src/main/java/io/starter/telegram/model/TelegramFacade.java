package io.starter.telegram.model;

import java.util.Objects;

import io.starter.telegram.cash.BotStateCash;
import io.starter.telegram.handler.CallbackQueryHandler;
import io.starter.telegram.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramFacade {

  private final CallbackQueryHandler callbackQueryHandler;
  private final MessageHandler messageHandler;
  private final BotStateCash botStateCash;

  public TelegramFacade(CallbackQueryHandler callbackQueryHandler,
                        MessageHandler messageHandler,
                        BotStateCash botStateCash) {
    this.callbackQueryHandler = callbackQueryHandler;
    this.messageHandler = messageHandler;
    this.botStateCash = botStateCash;
  }

  public BotApiMethod<?> handleUpdate(Update update) {
    if (update.hasCallbackQuery()) {
      CallbackQuery callbackQuery = update.getCallbackQuery();
      log.info("Received {}", callbackQuery);
      return callbackQueryHandler.processCallbackQuery(callbackQuery);
    } else {
      Message message = update.getMessage();
      log.info("Received {}", message);
      if (message.hasText()) {
        return handleInputMessage(message);
      }
    }
    return null;
  }

  private BotApiMethod<?> handleInputMessage(Message message) {
    final State state = Objects.requireNonNull(State.byText(message.getText()));
    switch (state) {
      case START:
        botStateCash.saveState(message, State.START);
        break;
      case SKILLS:
        botStateCash.saveState(message, State.SKILLS);
        break;
    }
    return messageHandler.handle(message, botStateCash.getCurrentState(message.getFrom()));
  }
}
