package io.starter.telegram.model;

import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Slf4j
@ToString
public class Telegram extends SpringWebhookBot {

  private final TelegramFacade FACADE;
  @Setter
  private String botPath;
  @Setter
  private String botUsername;
  @Setter
  private String botToken;

  public Telegram(TelegramFacade facade, SetWebhook webhook, String botToken) {
    super(webhook, botToken);
    this.FACADE = facade;
  }

  @Override
  public BotApiMethod onWebhookUpdateReceived(Update update) {
    return FACADE.handleUpdate(update);
  }

  @Override
  public String getBotPath() {
    return botPath;
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }
}
