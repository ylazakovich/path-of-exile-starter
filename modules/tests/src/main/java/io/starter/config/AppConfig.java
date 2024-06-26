package io.starter.config;

import org.aeonbits.owner.ConfigFactory;

public class AppConfig {

  private static final AppConfiguration CONFIG =
      ConfigFactory.create(AppConfiguration.class, System.getProperties());

  public static final String BASE_TELEGRAM_URL = CONFIG.baseTelegramUrl();
  public static final String WEBHOOK_URL = CONFIG.webhookUrl();
  public static final String ACCOUNT_PHONE = CONFIG.accountPhone();
}
