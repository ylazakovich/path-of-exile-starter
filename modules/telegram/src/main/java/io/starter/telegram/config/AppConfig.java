package io.starter.telegram.config;

import io.starter.telegram.model.telegram.Telegram;
import io.starter.telegram.model.telegram.TelegramFacade;
import io.starter.telegram.service.WebhookService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AppConfig {

  private final WebhookService webhookService;

  public AppConfig(WebhookService webhookService) {
    this.webhookService = webhookService;
  }

  @Bean
  public Telegram springWebhookBot(TelegramFacade telegramFacade) {
    return new Telegram(BotConfig.TOKEN, telegramFacade::handleOnUpdate, setWebhook());
  }

  private Runnable setWebhook() {
    webhookService.assignToken();
    return null;
  }
}
