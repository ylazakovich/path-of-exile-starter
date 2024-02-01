package io.starter.telegram.handler;

import io.starter.telegram.dao.UserDAO;
import io.starter.telegram.model.State;
import io.starter.telegram.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class MessageHandler {

  private final UserDAO userRepo;
  private final MenuService menu;

  public MessageHandler(UserDAO userRepo,
                        MenuService menu) {
    this.userRepo = userRepo;
    this.menu = menu;
  }

  public BotApiMethod<?> handle(Message message, State.Message state) {
    // TODO: might be it can be moved to higher level;
    userRepo.addIfNotExist(message.getFrom());
    return switch (state) {
      case START -> menu.getStart(message);
      case SKILLS -> menu.getSkills(message);
      default -> throw new IllegalStateException("Unexpected value: " + state);
    };
  }
}
