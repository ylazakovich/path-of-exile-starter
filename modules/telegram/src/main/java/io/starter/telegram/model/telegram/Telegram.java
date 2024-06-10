package io.starter.telegram.model.telegram;

import java.util.function.Function;

import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.webhook.starter.SpringTelegramWebhookBot;

@Slf4j
@ToString
public class Telegram extends SpringTelegramWebhookBot {

  private final TelegramFacade FACADE;
  @Setter
  private String botPath;
  @Setter
  private String botUsername;
  @Setter
  private String botToken;

  public Telegram(TelegramFacade facade, SetWebhook webhook, String botToken) {
    this.FACADE = facade;
  }

  public Function<Update, BotApiMethod<?>> onWebhookUpdateReceived() {
    return FACADE::handleOnUpdate;
  }

  @Override
  public String getBotPath() {
    return botPath;
  }
}
