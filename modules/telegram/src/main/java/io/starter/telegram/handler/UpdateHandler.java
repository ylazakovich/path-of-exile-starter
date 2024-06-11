package io.starter.telegram.handler;

import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.cash.state.MessageState;
import io.starter.telegram.dao.UserDao;
import io.starter.telegram.service.AnswerService;

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

  private final AnswerService menu;
  private final UserDao userDAO;

  public UpdateHandler(AnswerService menu,
                       UserDao userDAO) {
    this.userDAO = userDAO;
    this.menu = menu;
  }

  public BotApiMethod<?> handleOnUpdate(Message message, MessageState state) {
    final User user = message.getFrom();
    userDAO.saveWhenNotExist(user);
    userDAO.saveLastMessageId(user, message);
    return switch (state) {
      case WELCOME -> menu.messageOnFirstStart(message);
      case START -> menu.messageOnClickStart(message);
      case NO_CMD -> null;
      default -> throw new IllegalStateException("Unexpected value: " + state);
    };
  }

  public BotApiMethod<?> handleOnUpdate(CallbackQuery callback, CallbackState state) {
    final User user = callback.getFrom();
    final MaybeInaccessibleMessage message = callback.getMessage();
    userDAO.saveWhenNotExist(user);
    userDAO.saveLastMessageId(user, message);
    return switch (state) {
      case SKILLS -> menu.messageOnClickSkills(message);
      case ALL_SKILLS, REFRESH_SKILLS -> menu.callableMessageOnClickSkills(callback);
      case NO_CMD -> null;
      default -> throw new IllegalStateException("Unexpected value: " + state);
    };
  }
}
