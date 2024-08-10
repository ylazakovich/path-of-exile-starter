package io.starter.telegram.handler;

import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.cash.state.MessageState;
import io.starter.telegram.dao.UserDao;
import io.starter.telegram.service.OnCallbackAnswerService;
import io.starter.telegram.service.OnMessageAnswerService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.MaybeInaccessibleMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
@Slf4j
public class UpdateHandler {

  private final OnMessageAnswerService onMessageAnswerService;
  private final OnCallbackAnswerService onCallbackAnswerService;
  private final UserDao userDao;

  public UpdateHandler(OnMessageAnswerService onMessageAnswerService,
                       OnCallbackAnswerService onCallbackAnswerService,
                       UserDao userDao) {
    this.userDao = userDao;
    this.onMessageAnswerService = onMessageAnswerService;
    this.onCallbackAnswerService = onCallbackAnswerService;
  }

  public BotApiMethod<?> handleOnUpdate(Message message, MessageState state) {
    final User user = message.getFrom();
    userDao.saveWhenNotExist(user);
    userDao.saveLastMessageId(user, message);
    return switch (state) {
      case WELCOME -> onMessageAnswerService.onFirstStart(message);
      case START -> onMessageAnswerService.onClickStart(message);
      case NO_CMD -> null;
      default -> null;
    };
  }

  public BotApiMethod<?> handleOnUpdate(CallbackQuery callback, CallbackState state) {
    final User user = callback.getFrom();
    final MaybeInaccessibleMessage message = callback.getMessage();
    userDao.saveWhenNotExist(user);
    userDao.saveLastMessageId(user, message);
    return switch (state) {
      case SKILLS -> onMessageAnswerService.onClickSkills(message);
      case ALL_SKILLS, REFRESH_SKILLS -> onCallbackAnswerService.onClickSkills(callback);
      case NO_CMD -> null;
      default -> null;
    };
  }
}
