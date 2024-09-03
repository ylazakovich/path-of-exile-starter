package io.starter.telegram.model.telegram;

import java.util.function.Function;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.webhook.starter.SpringTelegramWebhookBot;

@Slf4j
@ToString
public class Telegram extends SpringTelegramWebhookBot {

  public Telegram(String token, Function<Update, BotApiMethod<?>> updateHandler, Runnable setWebhook) {
    super("bot%s".formatted(token), updateHandler, setWebhook, null);
  }
}
