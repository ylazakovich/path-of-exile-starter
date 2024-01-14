package io.automation.config;

import io.automation.model.Telegram;
import io.automation.model.TelegramFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
@Slf4j
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
    // TODO: need to check, might be not need to set these values below
    bot.setBotUsername("@" + config.userName);
    bot.setBotToken(config.botToken);
    bot.setBotPath(config.webHookPath);
    return bot;
  }
}
