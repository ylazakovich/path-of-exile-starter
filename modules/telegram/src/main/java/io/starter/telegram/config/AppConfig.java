package io.starter.telegram.config;

import io.starter.telegram.model.telegram.Telegram;
import io.starter.telegram.model.telegram.TelegramFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AppConfig {

  private final TelegramConfig config;

  public AppConfig(TelegramConfig config) {
    this.config = config;
  }

  @Bean
  public Telegram springWebhookBot(TelegramFacade telegramFacade) {
    return new Telegram(telegramFacade, config.token);
  }
}
