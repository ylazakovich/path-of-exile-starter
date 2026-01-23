package io.starter.controller;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.starter.model.telegram.Telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@RestController
@Slf4j
public class WebhookController {

  private final Telegram telegram;
  private final ObjectMapper telegramObjectMapper;

  public WebhookController(Telegram telegram, ObjectMapper telegramObjectMapper) {
    this.telegram = telegram;
    this.telegramObjectMapper = telegramObjectMapper;
  }

  @PostMapping("/")
  public BotApiMethod<?> onUpdateReceived(@RequestBody byte[] payload) {
    Update update;
    try {
      String jsonPayload = new String(payload, StandardCharsets.UTF_8);
      update = telegramObjectMapper.readValue(jsonPayload, Update.class);
    } catch (Exception exception) {
      log.error("Failed to deserialize telegram update payload", exception);
      return null;
    }
    if (update.hasCallbackQuery()) {
      CallbackQuery callbackQuery = update.getCallbackQuery();
      log.info("Telegram has catch callback with id ['{}']", callbackQuery.getId());
    } else if (Objects.isNull(update.getMessage())) {
      log.warn("Telegram has catch update without message {}", update);
      return null;
    } else {
      Message message = update.getMessage();
      log.info("Telegram has catch message with id ['{}']", message.getMessageId());
    }
    return telegram.consumeUpdate(update);
  }
}
