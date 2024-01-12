package io.automation.telegram.config;

import io.automation.telegram.model.Telegram;
import io.automation.telegram.model.TelegramFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
public class AppConfig {

  private final TelegramConfig config;

  public AppConfig(TelegramConfig config) {
    this.config = config;
  }

  @Bean
  public SetWebhook setWebhookInstance() {
    return SetWebhook.builder().url(config.webHookPath).build();
  }

  @Bean
  public Telegram springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
    Telegram bot = new Telegram(telegramFacade, setWebhook, config.botToken);
    bot.setBotUsername(config.userName);
    bot.setBotPath(config.webHookPath);
    return bot;
  }
}
