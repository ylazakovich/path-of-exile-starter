package io.starter.telegram.handler;

import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.cash.state.MessageState;
import io.starter.telegram.dao.UserDao;
import io.starter.telegram.service.CallbackAnswerService;
import io.starter.telegram.service.MessageAnswerService;

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

  private final MessageAnswerService messageAnswerService;
  private final CallbackAnswerService callbackAnswerService;
  private final UserDao userDao;

  public UpdateHandler(MessageAnswerService messageAnswerService,
                       CallbackAnswerService callbackAnswerService,
                       UserDao userDao) {
    this.userDao = userDao;
    this.messageAnswerService = messageAnswerService;
    this.callbackAnswerService = callbackAnswerService;
  }

  public BotApiMethod<?> handleOnUpdate(Message message, MessageState state) {
    final User user = message.getFrom();
    userDao.saveWhenNotExist(user);
    userDao.saveLastMessageId(user, message);
    return switch (state) {
      case WELCOME -> messageAnswerService.onFirstStart(message);
      case START -> messageAnswerService.onClickStart(message);
      case SETTINGS -> messageAnswerService.onClickSettings(message);
      default -> null;
    };
  }

  public BotApiMethod<?> handleOnUpdate(CallbackQuery callback, CallbackState state) {
    final User user = callback.getFrom();
    final MaybeInaccessibleMessage message = callback.getMessage();
    userDao.saveWhenNotExist(user);
    userDao.saveLastMessageId(user, message);
    return switch (state) {
      case SETTING_STANDARD, SETTING_LEAGUE, SETTING_HARDCORE, SETTING_LEAGUE_HARDCORE
          -> callbackAnswerService.onClickSetting(callback);
      case SKILLS -> messageAnswerService.onClickSkills(message);
      case ALL_SKILLS, REFRESH_SKILLS, SKILLS_PREVIOUS, SKILLS_NEXT -> callbackAnswerService.onClickSkills(callback);
      default -> null;
    };
  }
}
