package io.starter.config;

import org.aeonbits.owner.ConfigFactory;

public class AppConfig {

  private static final AppConfiguration CONFIG =
      ConfigFactory.create(AppConfiguration.class, System.getProperties());

  public static final String A8R_BASE_URL = CONFIG.a8rBaseUrl();
  public static final String WEBHOOK_URL = CONFIG.webhookUrl();
  public static final String ACCOUNT_PHONE = CONFIG.accountPhone();
  public static final String BOT_USERNAME = CONFIG.botUsername();
}
