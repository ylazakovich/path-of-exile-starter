package io.starter.telegram.handler;

import io.starter.telegram.dao.UserDAO;
import io.starter.telegram.model.State;
import io.starter.telegram.service.MenuService;
import io.starter.telegram.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class MessageHandler {

  private final UserDAO userRepo;
  private final MenuService menu;
  private final MessageService messageService;

  public MessageHandler(UserDAO userRepo,
                        MenuService menu,
                        MessageService messageService) {
    this.userRepo = userRepo;
    this.menu = menu;
    this.messageService = messageService;
  }

  public BotApiMethod<?> handle(Message message, State state) {
    // TODO: might be it can be moved to higher level;
    userRepo.addIfNotExist(message.getFrom());
    return switch (state) {
      case START -> menu.getStart(message);
      case SKILLS -> menu.getSkills(message);
      // TODO: wait for implementation
      case SKILLS_ALL -> new SendMessage();
      case SKILLS_WAIT_FOR_CMD -> new SendMessage();
      case WAIT_FOR_CMD -> new SendMessage();
      case SKILLS_ANY -> new SendMessage();
      case NO_CMD -> new SendMessage();
      default -> throw new IllegalStateException("Unexpected value: " + state);
    };
  }
}
