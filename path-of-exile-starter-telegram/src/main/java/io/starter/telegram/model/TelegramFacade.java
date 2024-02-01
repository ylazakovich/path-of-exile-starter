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
      // TODO: need to have a look at guide about callbacks
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
        botStateCash.saveState(message.getFrom().getId(), State.START);
        break;
      case SKILLS:
        botStateCash.saveState(message.getFrom().getId(), State.SKILLS);
        break;
    }
    return messageHandler.handle(message, botStateCash.getCurrentState(message.getFrom()));
  }
}
