package io.starter.telegram.model.telegram;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.webhook.starter.SpringTelegramWebhookBot;

@Slf4j
@ToString
public class Telegram extends SpringTelegramWebhookBot {

  public Telegram(TelegramFacade facade, String token) {
    super("bot%s".formatted(token), facade::handleOnUpdate, null, null);
  }
}
