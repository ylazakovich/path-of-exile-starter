package io.starter.model.handler;

import io.starter.dao.UserDAO;
import io.starter.model.State;
import io.starter.service.MenuService;
import io.starter.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Slf4j
public class MessageHandler {

  private final UserDAO userRepo;
  private final MenuService menuService;
  private final MessageService messageService;

  public MessageHandler(UserDAO userRepo,
                        MenuService menuService,
                        MessageService messageService) {
    this.userRepo = userRepo;
    this.menuService = menuService;
    this.messageService = messageService;
  }

  public BotApiMethod<?> handle(Message message, State state) {
    // TODO: might be it can be moved to higher level;
    userRepo.addIfNotExist(message.getFrom());
    return switch (state) {
      case START -> menuService.startMenu(message);
      case SKILLS_WAIT_FOR_CMD -> menuService.menuWithSkills(message);
      case SKILLS_ALL -> messageService.messageWithReadySkillsForTrade(message);
      // TODO: wait for implementation
      case WAIT_FOR_CMD -> new SendMessage();
      case SKILLS_ANY -> new SendMessage();
      case NO_CMD -> new SendMessage();
      default -> throw new IllegalStateException("Unexpected value: " + state);
    };
  }
}
