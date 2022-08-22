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

    @Value("${telegrambot.adminId}")
    private int adminId;


    public TelegramFacade(MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler, BotStateCash botStateCash) {
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
        BotState botState;
        String inputMsg = message.getText();
        //we process messages of the main menu and any other messages
        //set state
        switch (inputMsg) {
            case "/start":
                botState = BotState.START;
                break;
            case "Мои напоминания":
                botState = BotState.MYEVENTS;
                break;
            case "Создать напоминание":
                botState = BotState.CREATE;
                break;
            case "Отключить напоминания":
            case "Включить напоминания":
                botState = BotState.ONEVENT;
                break;
            case "All users":
                if (message.getFrom().getId() == adminId)
                    botState = BotState.ALLUSERS;
                else botState = BotState.START;
                break;
            case "All events":
                if (message.getFrom().getId() == adminId)
                    botState = BotState.ALLEVENTS;
                else botState = BotState.START;
                break;
            default:
                botState = botStateCash.getBotStateMap().get(message.getFrom().getId()) == null?
                        BotState.START: botStateCash.getBotStateMap().get(message.getFrom().getId());
        }
        //we pass the corresponding state to the handler
        //the corresponding method will be called
        return messageHandler.handle(message, botState);

    }
}
