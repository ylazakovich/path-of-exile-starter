package io.starter.telegram.model.telegram;

import java.util.Objects;

import io.starter.telegram.cash.CallbackCash;
import io.starter.telegram.cash.MessageCash;
import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.cash.state.MessageState;
import io.starter.telegram.handler.UpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramFacade {

  private final UpdateHandler updateHandler;
  private final CallbackCash callbackCash;
  private final MessageCash messageCash;

  public TelegramFacade(UpdateHandler updateHandler,
                        CallbackCash callbackCash,
                        MessageCash messageCash) {
    this.updateHandler = updateHandler;
    this.callbackCash = callbackCash;
    this.messageCash = messageCash;
  }

  public BotApiMethod<?> handleUpdate(Update update) {
    if (update.hasCallbackQuery()) {
      CallbackQuery callbackQuery = update.getCallbackQuery();
      log.info("Received {}", callbackQuery);
      return handleCallbackQuery(callbackQuery);
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
      default:
        messageCash.saveState(message, MessageState.FIRST_START);
        break;
    }
    return updateHandler.handle(message, messageCash.getCurrentState(message.getFrom()));
  }

  public BotApiMethod<?> handleCallbackQuery(CallbackQuery query) {
    final CallbackState state = Objects.requireNonNull(CallbackState.byData(query.getData()));
    switch (state) {
      case SKILLS:
        callbackCash.saveState(query, CallbackState.SKILLS);
        break;
      case SKILLS_ALL:
        callbackCash.saveState(query, CallbackState.SKILLS_ALL);
        break;
      default:
        callbackCash.saveState(query, CallbackState.NO_CMD);
        break;
    }
    return updateHandler.handle(query, callbackCash.getCurrentState(query.getFrom()));
  }
}
