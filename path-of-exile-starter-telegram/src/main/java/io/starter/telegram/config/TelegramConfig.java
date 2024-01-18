package io.starter.telegram.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramConfig {

  @Value("${telegram.webhook}")
  public String webHook;
  @Value("${telegram.token}")
  public String token;
}
