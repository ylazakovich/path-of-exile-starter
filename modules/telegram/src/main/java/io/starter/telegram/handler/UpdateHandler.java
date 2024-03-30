package io.starter.telegram.handler;

import io.starter.telegram.cash.state.CallbackState;
import io.starter.telegram.cash.state.MessageState;
import io.starter.telegram.dao.UserDAO;
import io.starter.telegram.service.MenuService;
import io.starter.telegram.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class UpdateHandler {

  private final MessageService messageService;
  private final MenuService menu;
  private final UserDAO userRepo;

  public UpdateHandler(MessageService messageService,
                       MenuService menu,
                       UserDAO userRepo) {
    this.messageService = messageService;
    this.userRepo = userRepo;
    this.menu = menu;
  }

  public BotApiMethod<?> handle(Message message, MessageState state) {
    userRepo.addIfNotExist(message.getFrom());
    return switch (state) {
      case FIRST_START -> menu.getMain(message);
      case START -> menu.getStart(message);
      default -> throw new IllegalStateException("Unexpected value: " + state);
    };
  }

  public BotApiMethod<?> handle(CallbackQuery query, CallbackState state) {
    userRepo.addIfNotExist(query.getFrom());
    return switch (state) {
      case SKILLS -> messageService.messageWithSkillGuide(query);
      case SKILLS_ALL -> messageService.messageWithReadySkillsForTrade(query);
      case SKILLS_ANY -> null;
      case NO_CMD -> null;
      default -> throw new IllegalStateException("Unexpected value: " + state);
    };
  }
}
