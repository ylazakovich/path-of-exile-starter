package io.automation.telegram.botconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotConfig {

  @Value("${telegrambot.webHookPath}")
  public String webHookPath;
  @Value("${telegrambot.userName}")
  public String userName;
  @Value("${telegrambot.botToken}")
  public String botToken;
}
