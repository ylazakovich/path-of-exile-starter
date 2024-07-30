package io.starter.telegram.service;

import io.starter.telegram.config.BotConfig;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebhookService {

  private final WebClient client;

  public WebhookService() {
    this.client = WebClient.builder()
        .baseUrl("https://api.telegram.org")
        .build();
  }

  public void assignToken() {
    client.post()
        .uri("/bot%s/setWebhook?url=%s".formatted(BotConfig.TOKEN, BotConfig.WEBHOOK))
        .retrieve()
        .toBodilessEntity()
        .subscribe();
  }
}
