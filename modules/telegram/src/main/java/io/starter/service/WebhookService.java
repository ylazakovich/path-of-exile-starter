package io.starter.service;

import io.starter.config.BotConfig;
import io.starter.config.third_party.TelegramConfig;

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
