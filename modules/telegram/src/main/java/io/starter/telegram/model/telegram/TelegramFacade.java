package io.starter.telegram.model.telegram;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import io.starter.telegram.cache.CallbackCache;
import io.starter.telegram.cache.MessageCache;
import io.starter.telegram.cache.state.CallbackState;
import io.starter.telegram.cache.state.MessageState;
import io.starter.telegram.handler.UpdateHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@Slf4j
public class TelegramFacade {

  private final UpdateHandler updateHandler;
  private final BiConsumer<Message, MessageState> messageConsumer;
  private final BiConsumer<CallbackQuery, CallbackState> callbackConsumer;
  private final Function<User, CallbackState> callbackHandler;
  private final Function<User, MessageState> messageHandler;

  public TelegramFacade(UpdateHandler updateHandler,
                        CallbackCache callbackCache,
                        MessageCache messageCache) {
    this.updateHandler = updateHandler;
    this.messageConsumer = messageCache::saveState;
    this.callbackConsumer = callbackCache::saveState;
    this.messageHandler = messageCache::getCurrentState;
    this.callbackHandler = callbackCache::getCurrentState;
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
    messageConsumer.accept(message, state);
    return updateHandler.handleOnUpdate(message, messageHandler.apply(user));
  }

  private BotApiMethod<?> handleOnCallback(CallbackQuery callback) {
    final CallbackState state = Objects.requireNonNull(CallbackState.byData(callback.getData()));
    final User user = callback.getFrom();
    callbackConsumer.accept(callback, state);
    return updateHandler.handleOnUpdate(callback, callbackHandler.apply(user));
  }
}
