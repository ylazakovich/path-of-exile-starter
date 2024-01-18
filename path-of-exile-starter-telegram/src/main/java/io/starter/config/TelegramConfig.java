package io.starter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramConfig {

  @Value("${telegram.webhook}")
  public String webHookPath;
  @Value("${telegram.token}")
  public String botToken;
}
