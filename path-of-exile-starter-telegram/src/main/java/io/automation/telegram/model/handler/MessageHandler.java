package io.automation.telegram.model.handler;

import io.automation.telegram.cash.BotStateCash;
import io.automation.telegram.dao.UserDAO;
import io.automation.telegram.model.State;
import io.automation.telegram.service.MenuService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MessageHandler {

  private final UserDAO userDAO;
  private final MenuService menuService;
  private final EventHandler eventHandler;
  private final BotStateCash botStateCash;

  public MessageHandler(UserDAO userDAO,
                        MenuService menuService,
                        EventHandler eventHandler,
                        BotStateCash botStateCash) {
    this.userDAO = userDAO;
    this.menuService = menuService;
    this.eventHandler = eventHandler;
    this.botStateCash = botStateCash;
  }

  public BotApiMethod<?> handle(Message message, State state) {
    long userId = message.getFrom().getId();
    long chatId = message.getChatId();
    SendMessage sendMessage = new SendMessage();
    sendMessage.setChatId(String.valueOf(chatId));
    if (!userDAO.isExist(userId)) {
      return eventHandler.saveNewUser(message, userId, sendMessage);
    }
    botStateCash.saveBotState(userId, state);
    switch (state.name()) {
      case "START":
        return menuService.getMainMenuMessage(message.getChatId(), "Воспользуйтесь главным меню", userId);
      case "ENTERTIME":
        //set time zone user. for correct sent event
        return eventHandler.enterLocalTimeUser(message);
      case "ALLUSERS":
        //only admin
        return eventHandler.allUsers(userId);
      case "ONEVENT":
        // on/off notification
        return eventHandler.onEvent(message);
      case "ENTERNUMBERUSER":
        //only admin
        return eventHandler.removeUserHandler(message, userId);
      default:
        throw new IllegalStateException("Unexpected value: " + state);
    }
  }
}
