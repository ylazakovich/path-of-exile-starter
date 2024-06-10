package io.starter.telegram.handler;

import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.cash.state.MessageState;
import io.starter.telegram.dao.UserDao;
import io.starter.telegram.service.MenuService;
import io.starter.telegram.service.MessageService;
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

  private final MessageService messageService;
  private final MenuService menu;
  private final UserDao userDAO;

  public UpdateHandler(MessageService messageService,
                       MenuService menu,
                       UserDao userDAO) {
    this.messageService = messageService;
    this.userDAO = userDAO;
    this.menu = menu;
  }

  public BotApiMethod<?> handleOnUpdate(Message message, MessageState state) {
    final User user = message.getFrom();
    userDAO.saveWhenNotExist(user);
    userDAO.saveLastMessageId(user, message);
    return switch (state) {
      case WELCOME -> menu.getMain(message);
      case START -> menu.getStart(message);
      default -> throw new IllegalStateException("Unexpected value: " + state);
    };
  }

  public BotApiMethod<?> handleOnUpdate(CallbackQuery callback, CallbackState state) {
    final User user = callback.getFrom();
    final MaybeInaccessibleMessage message = callback.getMessage();
    userDAO.saveWhenNotExist(user);
    userDAO.saveLastMessageId(user, message);
    return switch (state) {
      case SKILLS -> menu.getMenuWithSkills(message);
      case ALL_SKILLS -> messageService.messageWithReadySkillsForTrade(callback);
      case NO_CMD -> null;
      default -> throw new IllegalStateException("Unexpected value: " + state);
    };
  }
}
