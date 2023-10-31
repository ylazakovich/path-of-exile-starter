package io.automation.telegram.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import io.automation.telegram.model.Telegram;

@RestController
public class WebhookController {

  private final Telegram telegram;

  public WebhookController(Telegram telegram) {
    this.telegram = telegram;
  }

  // point for message
  @PostMapping("/")
  public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
    return telegram.onWebhookUpdateReceived(update);
  }

  @GetMapping
  public ResponseEntity get() {
    return ResponseEntity.ok().build();
  }
}
