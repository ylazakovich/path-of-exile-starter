package io.starter.telegram.handler;

import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.cash.state.MessageState;
import io.starter.telegram.dao.UserDAO;
import io.starter.telegram.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class UpdateHandler {

  private final UserDAO userRepo;
  private final MenuService menu;

  public UpdateHandler(UserDAO userRepo,
                       MenuService menu) {
    this.userRepo = userRepo;
    this.menu = menu;
  }

  public BotApiMethod<?> handle(Message message, MessageState state) {
    userRepo.addIfNotExist(message.getFrom());
    return switch (state) {
      case START -> menu.getStart(message);
      case SKILLS -> menu.getSkills(message);
      default -> throw new IllegalStateException("Unexpected value: " + state);
    };
  }

  public BotApiMethod<?> handle(CallbackQuery query, CallbackState state) {
    userRepo.addIfNotExist(query.getFrom());
    return switch (state) {
      case SKILL_ALL -> null;
      case SKILLS_ANY -> null;
      default -> throw new IllegalStateException("Unexpected value: " + state);
    };
  }
}
