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
import org.telegram.telegrambots.meta.api.objects.User;

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

  public BotApiMethod<?> handleOnUpdate(Update update) {
    if (update.hasCallbackQuery()) {
      CallbackQuery callback = update.getCallbackQuery();
      User user = callback.getFrom();
      log.info("Query received by user [id: {} - username: '{}' - 'name: {} {}'] user made ['{}']",
          user.getId(),
          user.getUserName(),
          user.getFirstName(),
          user.getLastName(),
          callback.getData());
      return handleOnCallback(callback);
    } else {
      Message message = update.getMessage();
      User user = message.getFrom();
      log.info("Message received by user [id: '{}' - username: '{}' - name: '{} {}'] - user send ['{}']",
          user.getId(),
          user.getUserName(),
          user.getFirstName(),
          user.getLastName(),
          message.getText());
      return handleOnMessage(message);
    }
  }

  private BotApiMethod<?> handleOnMessage(Message message) {
    final MessageState state = Objects.requireNonNull(MessageState.byText(message.getText()));
    final User user = message.getFrom();
    switch (state) {
      case START:
        messageCash.saveState(message, MessageState.START);
        break;
      default:
        messageCash.saveState(message, MessageState.WELCOME);
        break;
    }
    return updateHandler.handleOnUpdate(message, messageCash.getCurrentState(user));
  }

  public BotApiMethod<?> handleOnCallback(CallbackQuery callback) {
    final CallbackState state = Objects.requireNonNull(CallbackState.byData(callback.getData()));
    final User user = callback.getFrom();
    switch (state) {
      case SKILLS:
        callbackCash.saveState(callback, CallbackState.SKILLS);
        break;
      case All_SKILLS:
        callbackCash.saveState(callback, CallbackState.All_SKILLS);
        break;
      default:
        callbackCash.saveState(callback, CallbackState.NO_CMD);
        break;
    }
    return updateHandler.handleOnUpdate(callback, callbackCash.getCurrentState(user));
  }
}
