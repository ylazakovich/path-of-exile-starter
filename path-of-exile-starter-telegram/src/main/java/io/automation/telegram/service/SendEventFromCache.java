package io.automation.telegram.service;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;

import io.automation.telegram.dao.EventCashDAO;
import io.automation.telegram.entity.EventCashEntity;
import io.automation.telegram.model.Telegram;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendEventFromCache {

  private final EventCashDAO eventCashDAO;
  private final Telegram telegram;

  @Value("${telegram.adminId}")
  private int admin_id;

  @Autowired
  public SendEventFromCache(EventCashDAO eventCashDAO, Telegram telegram) {
    this.eventCashDAO = eventCashDAO;
    this.telegram = telegram;
  }

  @PostConstruct
  //after every restart app  - check unspent events
  private void afterStart() {
    List<EventCashEntity> list = eventCashDAO.findAll();

    try {
      SendMessage sendMessage = new SendMessage();
      sendMessage.setChatId(String.valueOf(admin_id));
      sendMessage.setText("Произошла перезагрузка!");
      telegram.execute(sendMessage);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }

    if (!list.isEmpty()) {
      for (EventCashEntity eventCashEntity : list) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(eventCashEntity.date);
        SendEvent sendEvent = new SendEvent();
        sendEvent.setSendMessage(
            new SendMessage(String.valueOf(eventCashEntity.userId), eventCashEntity.description));
        sendEvent.setEventCashId(eventCashEntity.id);
        new Timer().schedule(new SimpleTask(sendEvent), calendar.getTime());
      }
    }
  }
}
