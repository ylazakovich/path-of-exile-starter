package io.automation.telegram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import io.automation.telegram.botconfig.TelegramBotConfig;
import io.automation.telegram.model.Telegram;
import io.automation.telegram.model.TelegramFacade;

@Configuration
public class AppConfig {

  private final TelegramBotConfig TELEGRAM_CONFIG;

  public AppConfig(TelegramBotConfig TELEGRAM_CONFIG) {
    this.TELEGRAM_CONFIG = TELEGRAM_CONFIG;
  }

  @Bean
  public SetWebhook setWebhookInstance() {
    return SetWebhook.builder().url(TELEGRAM_CONFIG.webHookPath).build();
  }

  @Bean
  public Telegram springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
    Telegram bot = new Telegram(telegramFacade, setWebhook);
    bot.setBotToken(TELEGRAM_CONFIG.botToken);
    bot.setBotUsername(TELEGRAM_CONFIG.userName);
    bot.setBotPath(TELEGRAM_CONFIG.webHookPath);
    return bot;
  }
}
