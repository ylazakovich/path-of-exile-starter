package io.automation.telegram.model.handler;

import io.automation.telegram.cash.BotStateCash;
import io.automation.telegram.cash.EventCash;
import io.automation.telegram.entity.Event;
import io.automation.telegram.model.State;
import io.automation.telegram.model.EventFreq;
import io.automation.telegram.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
//processes incoming callback's
public class CallbackQueryHandler {
    private final BotStateCash botStateCash;
    private final EventCash eventCash;
    private final MenuService menuService;
    private final EventHandler eventHandler;

    @Autowired
    public CallbackQueryHandler(BotStateCash botStateCash, EventCash eventCash, MenuService menuService, EventHandler eventHandler) {
        this.botStateCash = botStateCash;
        this.eventCash = eventCash;
        this.menuService = menuService;
        this.eventHandler = eventHandler;
    }

    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final long userId = buttonQuery.getFrom().getId();

        BotApiMethod<?> callBackAnswer = null;

        String data = buttonQuery.getData();

        switch (data) {
            case ("buttonDel"):
                callBackAnswer = new SendMessage(String.valueOf(chatId), "Введите номер напоминания из списка.");
                botStateCash.saveBotState(userId, State.ENTER_NUMBER_EVENT);
                break;
            case ("buttonDelUser"):
                callBackAnswer = new SendMessage(String.valueOf(chatId), "Enter ID User.");
                botStateCash.saveBotState(userId, State.ENTER_NUMBER_USER);
                break;
            case ("buttonEdit"):
                callBackAnswer = new SendMessage(String.valueOf(chatId), "Введите номер напоминания из списка.");
                botStateCash.saveBotState(userId, State.ENTER_NUMBER_FOR_EDIT);
                break;
            case ("buttonOneTime"):
                if (botStateCash.getBotStateMap().get(userId).name().equals("ENTERDATE")) {
                    callBackAnswer = eventHandler.saveEvent(EventFreq.TIME, userId, chatId);
                } else {
                    Event event = eventCash.getEventMap().get(userId);
                    event.freq = EventFreq.TIME;
                    eventCash.saveEventCash(userId, event);
                    callBackAnswer = eventHandler.editEvent(chatId, userId);
                }
                break;
            case ("buttonOneTimeMonth"):
                if (botStateCash.getBotStateMap().get(userId).name().equals("ENTERDATE")) {
                    callBackAnswer = eventHandler.saveEvent(EventFreq.MONTH, userId, chatId);
                } else {
                    Event event = eventCash.getEventMap().get(userId);
                    event.freq = EventFreq.MONTH;
                    eventCash.saveEventCash(userId, event);
                    callBackAnswer = eventHandler.editEvent(chatId, userId);
                }
                break;
            case ("buttonEveryDay"):
                if (botStateCash.getBotStateMap().get(userId).name().equals("ENTERDATE")) {
                    callBackAnswer = eventHandler.saveEvent(EventFreq.EVERYDAY, userId, chatId);
                } else {
                    Event event = eventCash.getEventMap().get(userId);
                    event.freq = EventFreq.EVERYDAY;
                    eventCash.saveEventCash(userId, event);
                    callBackAnswer = eventHandler.editEvent(chatId, userId);
                }
                break;
            case ("buttonOneTimeYear"):
                if (botStateCash.getBotStateMap().get(userId).name().equals("ENTERDATE")) {
                    callBackAnswer = eventHandler.saveEvent(EventFreq.YEAR, userId, chatId);
                } else {
                    Event event = eventCash.getEventMap().get(userId);
                    event.freq = EventFreq.YEAR;
                    eventCash.saveEventCash(userId, event);
                    callBackAnswer = eventHandler.editEvent(chatId, userId);
                }
                break;
            case ("buttonDate"):
                if (eventCash.getEventMap().get(userId).eventId != 0) {
                    callBackAnswer = new SendMessage(String.valueOf(chatId), "Введите дату " +
                            "предстоящего события в формате DD.MM.YYYY HH:MM, например - " +
                            "02.06.2021 21:24, либо 02.06.2021");
                    botStateCash.saveBotState(userId, State.EDIT_DATE);
                } else {
                    callBackAnswer = new SendMessage(String.valueOf(chatId),
                            "Нарушена последовательность действий");
                }
                break;
            case ("buttonDescription"):
                if (eventCash.getEventMap().get(userId).eventId != 0) {
                    callBackAnswer = new SendMessage(String.valueOf(chatId), "Введите описание события");
                    botStateCash.saveBotState(userId, State.EDIT_DESCRIPTION);
                } else {
                    callBackAnswer = new SendMessage(String.valueOf(chatId),
                            "Нарушена последовательность действий");
                }
                break;
            case ("buttonHour"):
                callBackAnswer = new SendMessage(String.valueOf(chatId), "Необходимо ввести местное время в формате HH, например, " +
                        "если сейчас 21:45, то введите 21, это необходимо для корректнрого оповещения в соответсвии с вашим часовым поясом.");
                botStateCash.saveBotState(userId, State.ENTER_TIME);
                break;
            case ("buttonFreq"):
                if (eventCash.getEventMap().get(userId).eventId != 0) {
                    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), "Выберите период повторения" +
                            "(Единоразово, 1 раз в месяц в указанную дату, 1 раз в год в указанное число)");
                    botStateCash.saveBotState(userId, State.EDIT_FREQ);
                    sendMessage.setReplyMarkup(menuService.getInlineMessageButtonsForEnterDate());
                    callBackAnswer = sendMessage;
                } else {
                    callBackAnswer = new SendMessage(String.valueOf(chatId),
                            "Нарушена последовательность действий");
                }
        }
        return callBackAnswer;
    }
}
