package io.starter.telegram.model;

import java.util.Objects;

import io.starter.telegram.cash.MessageCash;
import io.starter.telegram.cash.state.MessageState;
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
  private final MessageCash messageCash;
  private final MessageHandler messageHandler;

  public TelegramFacade(CallbackQueryHandler callbackQueryHandler,
                        MessageCash messageCash,
                        MessageHandler messageHandler) {
    this.callbackQueryHandler = callbackQueryHandler;
    this.messageCash = messageCash;
    this.messageHandler = messageHandler;
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
    final MessageState state = Objects.requireNonNull(MessageState.byText(message.getText()));
    switch (state) {
      case START:
        messageCash.saveState(message, MessageState.START);
        break;
      case SKILLS:
        messageCash.saveState(message, MessageState.SKILLS);
        break;
    }
    return messageHandler.handle(message, messageCash.getCurrentState(message.getFrom()));
  }
}
