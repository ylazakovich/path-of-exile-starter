package io.automation.telegram.model;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

public class Telegram extends SpringWebhookBot {

  private final TelegramFacade FACADE;
  private String botPath;
  private String botUsername;
  private String botToken;

  public Telegram(TelegramFacade facade, DefaultBotOptions options, SetWebhook webhook) {
    super(options, webhook);
    this.FACADE = facade;
  }

  public Telegram(TelegramFacade facade, SetWebhook webhook) {
    super(webhook);
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

  public Telegram setBotPath(String botPath) {
    this.botPath = botPath;
    return this;
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }

  public Telegram setBotUsername(String botUsername) {
    this.botUsername = botUsername;
    return this;
  }

  @Override
  public String getBotToken() {
    return botToken;
  }

  public Telegram setBotToken(String botToken) {
    this.botToken = botToken;
    return this;
  }
}
