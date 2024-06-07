package io.starter.telegram.controller;

import io.starter.telegram.dao.RegressionDao;
import io.starter.telegram.entity.RegressionEntity;
import io.starter.telegram.model.telegram.Telegram;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class WebhookController {

  private final Telegram telegram;
  private final RegressionDao regressionDao;

  public WebhookController(Telegram telegram,
                           RegressionDao regressionDao) {
    this.telegram = telegram;
    this.regressionDao = regressionDao;
  }

  @PostMapping("/")
  public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
    if (update.hasCallbackQuery()) {
      CallbackQuery callbackQuery = update.getCallbackQuery();
      log.info("Telegram has catch callback with id ['{}']", callbackQuery.getId());
    } else {
      Message message = update.getMessage();
      log.info("Telegram has catch message with id ['{}']", message.getMessageId());
    }
    return telegram.onWebhookUpdateReceived(update);
  }

  @GetMapping("/code")
  public ResponseEntity get() {
    log.info("Get verification code for running regression");
    RegressionEntity entity = regressionDao.read(1);
    return ResponseEntity.ok().body(entity);
  }
}
