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
  // TODO: SetWebhook may be is not needed in constructor
  public Telegram springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
    Telegram bot = new Telegram(telegramFacade, setWebhook, config.botToken);
    bot.setBotUsername("@" + config.userName);
    bot.setBotToken(config.botToken);
    bot.setBotPath(config.webHookPath);
    log.info("Bot settings: {}", bot);
    return bot;
  }
}
