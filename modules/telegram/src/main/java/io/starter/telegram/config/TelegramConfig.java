package io.starter.telegram.config;

import org.aeonbits.owner.ConfigFactory;

public class TelegramConfig {

  private static final TelegramConfiguration CONFIG =
      ConfigFactory.create(TelegramConfiguration.class, System.getProperties());

  public static final String BASE_URL = CONFIG.baseUrl();
}
