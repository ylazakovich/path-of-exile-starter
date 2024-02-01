package io.starter.telegram.model;

import java.util.Objects;

import io.starter.telegram.cash.BotMessageStateCash;
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
  private final BotMessageStateCash botMessageStateCash;

  public TelegramFacade(CallbackQueryHandler callbackQueryHandler,
                        MessageHandler messageHandler,
                        BotMessageStateCash botMessageStateCash) {
    this.callbackQueryHandler = callbackQueryHandler;
    this.messageHandler = messageHandler;
    this.botMessageStateCash = botMessageStateCash;
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
    final State.Message state = Objects.requireNonNull(State.Message.byText(message.getText()));
    switch (state) {
      case START:
        botMessageStateCash.saveState(message, State.Message.START);
        break;
      case SKILLS:
        botMessageStateCash.saveState(message, State.Message.SKILLS);
        break;
    }
    return messageHandler.handle(message, botMessageStateCash.getCurrentState(message.getFrom()));
  }
}
