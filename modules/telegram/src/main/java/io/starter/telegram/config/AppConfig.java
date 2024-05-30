package io.starter.telegram.config;

import io.starter.telegram.model.telegram.Telegram;
import io.starter.telegram.model.telegram.TelegramFacade;
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
    return SetWebhook.builder().url(config.webHook).build();
  }

  @Bean
  public Telegram springWebhookBot(SetWebhook setWebhook, TelegramFacade telegramFacade) {
    return new Telegram(telegramFacade, setWebhook, config.token);
  }
}
