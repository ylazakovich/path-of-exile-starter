package io.automation.telegram.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import io.automation.telegram.DAO.EventCashDAO;
import io.automation.telegram.config.ApplicationContextProvider;
import io.automation.telegram.model.Telegram;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//thread with event
public class SendEvent extends Thread {

  private long eventCashId;
  private SendMessage sendMessage;

  public SendEvent() {
  }

  public long getEventCashId() {
    return eventCashId;
  }

  public SendEvent setEventCashId(long eventCashId) {
    this.eventCashId = eventCashId;
    return this;
  }

  public SendMessage getSendMessage() {
    return sendMessage;
  }

  public SendEvent setSendMessage(SendMessage sendMessage) {
    this.sendMessage = sendMessage;
    return this;
  }

  @Override
  public void run() {
    Telegram telegram = ApplicationContextProvider.getContext().getBean(Telegram.class);
    EventCashDAO eventCashDAO = ApplicationContextProvider.getContext().getBean(EventCashDAO.class);
    try {
      telegram.execute(sendMessage);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
    //if event it worked, need to remove it from the database of unresolved events
    eventCashDAO.delete(eventCashId);
  }
}
