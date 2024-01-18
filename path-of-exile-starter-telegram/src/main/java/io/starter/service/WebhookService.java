package io.starter.service;

import io.starter.config.TelegramConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebhookService {

  private final WebClient client;
  private final TelegramConfig config;

  public WebhookService(TelegramConfig config) {
    this.config = config;
    this.client = WebClient.builder()
        .baseUrl("https://api.telegram.org")
        .build();
  }

  public void assignToken() {
    client.post()
        .uri("/bot%s/setWebhook?url=%s".formatted(config.token, config.webHook))
        .retrieve()
        .toBodilessEntity()
        .subscribe();
  }
}
