package io.automation.telegram.model.handler;

import io.automation.telegram.dao.UserDAO;
import io.automation.telegram.cash.BotStateCash;
import io.automation.telegram.cash.EventCash;
import io.automation.telegram.entity.Event;
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
  private final EventCash eventCash;

  public MessageHandler(UserDAO userDAO, MenuService menuService, EventHandler eventHandler, BotStateCash botStateCash,
                        EventCash eventCash) {
    this.userDAO = userDAO;
    this.menuService = menuService;
    this.eventHandler = eventHandler;
    this.botStateCash = botStateCash;
    this.eventCash = eventCash;
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
      case "MYEVENTS":
        //list events of user
        return eventHandler.myEventHandler(userId);
      case "ENTERNUMBEREVENT":
        //remove event
        return eventHandler.removeEventHandler(message, userId);
      case "ENTERDESCRIPTION":
        //enter description for create event
        return eventHandler.enterDescriptionHandler(message, userId);
      case "ENTERDATE":
        //enter date for create event
        return eventHandler.enterDateHandler(message, userId);
      case "CREATE":
        //start create event, set state to next step
        botStateCash.saveBotState(userId, State.ENTER_DESCRIPTION);
        //set new event to cache
        eventCash.saveEventCash(userId, new Event());
        sendMessage.setText("Введите описание события");
        return sendMessage;
      case "ENTERNUMBERFOREDIT":
        //show to user selected event
        return eventHandler.editHandler(message, userId);
      case "EDITDESCRIPTION":
        //save new description in database
        return eventHandler.editDescription(message);
      case "EDITDATE":
        //save new date in database
        return eventHandler.editDate(message);
      case "ALLEVENTS":
        //only admin
        return eventHandler.allEvents(userId);
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
