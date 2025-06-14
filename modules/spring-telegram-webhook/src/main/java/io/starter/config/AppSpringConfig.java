package io.starter.config;

import io.starter.model.telegram.Telegram;
import io.starter.model.telegram.TelegramFacade;
import io.starter.service.WebhookService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AppSpringConfig {

  private final WebhookService webhookService;

  public AppSpringConfig(WebhookService webhookService) {
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
