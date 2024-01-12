package io.automation.telegram.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramConfig {

  @Value("${telegram.webhook}")
  public String webHookPath;
  @Value("${telegram.userName}")
  public String userName;
  @Value("${telegram.token}")
  public String botToken;
}
