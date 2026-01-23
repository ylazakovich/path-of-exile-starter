package io.starter.handler;

import io.starter.cache.state.CallbackState;
import io.starter.cache.state.MessageState;
import io.starter.dao.UserDao;
import io.starter.service.CallbackAnswerService;
import io.starter.service.MessageAnswerService;

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
      case SKILLS, REFRESH_SKILLS, SKILLS_PREVIOUS, SKILLS_NEXT -> callbackAnswerService.onClickSkills(callback);
      case VENDOR_RECIPES -> callbackAnswerService.onClickVendorRecipes(callback);
      case ANIMA_STONE, REFRESH_ANIMA_STONE -> callbackAnswerService.onClickAnimaStone(callback);
      default -> null;
    };
  }
}
