package io.starter.telegram.service;

import io.starter.telegram.config.BotConfig;

import io.starter.telegram.config.TelegramConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebhookService {

  private static final String WEBHOOK = "/bot%s/setWebhook?url=%s";

  private final WebClient client;

  public WebhookService() {
    this.client = WebClient.builder()
        .baseUrl(TelegramConfig.BASE_URL)
        .build();
  }

  public void assignToken() {
    client.post()
        .uri(WEBHOOK.formatted(BotConfig.TOKEN, BotConfig.WEBHOOK))
        .retrieve()
        .toBodilessEntity()
        .subscribe();
  }
}
