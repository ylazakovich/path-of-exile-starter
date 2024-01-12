package io.automation.telegram.model;

import io.automation.telegram.cash.BotStateCash;
import io.automation.telegram.model.handler.CallbackQueryHandler;
import io.automation.telegram.model.handler.MessageHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramFacade {

  private final MessageHandler messageHandler;
  private final CallbackQueryHandler callbackQueryHandler;
  private final BotStateCash botStateCash;

  @Value("${telegram.adminId}")
  private int adminId;


  public TelegramFacade(MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler,
                        BotStateCash botStateCash) {
    this.messageHandler = messageHandler;
    this.callbackQueryHandler = callbackQueryHandler;
    this.botStateCash = botStateCash;
  }

  public BotApiMethod<?> handleUpdate(Update update) {

    if (update.hasCallbackQuery()) {
      CallbackQuery callbackQuery = update.getCallbackQuery();
      return callbackQueryHandler.processCallbackQuery(callbackQuery);
    } else {

      Message message = update.getMessage();
      if (message != null && message.hasText()) {
        return handleInputMessage(message);
      }
    }
    return null;
  }

  private BotApiMethod<?> handleInputMessage(Message message) {
    State state;
    String inputMsg = message.getText();
    //we process messages of the main menu and any other messages
    //set state
    switch (inputMsg) {
      case "/start":
        state = State.START;
        break;
      case "Мои напоминания":
        state = State.MY_EVENTS;
        break;
      case "Создать напоминание":
        state = State.CREATE_EVENT;
        break;
      case "Отключить напоминания":
      case "Включить напоминания":
        state = State.ON_EVENT;
        break;
      case "All users":
        if (message.getFrom().getId() == adminId) {
          state = State.ALL_USERS;
        } else {
          state = State.START;
        }
        break;
      case "All events":
        if (message.getFrom().getId() == adminId) {
          state = State.ALL_EVENTS;
        } else {
          state = State.START;
        }
        break;
      default:
        state = botStateCash.getBotStateMap().get(message.getFrom().getId()) == null ?
            State.START : botStateCash.getBotStateMap().get(message.getFrom().getId());
    }
    //we pass the corresponding state to the handler
    //the corresponding method will be called
    return messageHandler.handle(message, state);

  }
}
