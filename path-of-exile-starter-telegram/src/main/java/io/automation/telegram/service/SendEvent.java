package io.automation.telegram.service;

import io.automation.telegram.config.ApplicationContextProvider;
import io.automation.telegram.dao.EventCashDAO;
import io.automation.telegram.model.Telegram;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SendEvent extends Thread {

  private long eventCashId;
  private SendMessage sendMessage;

  @Override
  public void run() {
    Telegram telegram = ApplicationContextProvider.getContext().getBean(Telegram.class);
    EventCashDAO eventCashDAO = ApplicationContextProvider.getContext().getBean(EventCashDAO.class);
    try {
      telegram.execute(sendMessage);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
    eventCashDAO.delete(eventCashId);
  }
}