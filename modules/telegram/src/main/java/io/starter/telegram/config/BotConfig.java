package io.starter.telegram.config;

import org.aeonbits.owner.ConfigFactory;

public class BotConfig {

  private static final BotConfiguration CONFIG =
      ConfigFactory.create(BotConfiguration.class, System.getProperties());

  public static final String WEBHOOK = CONFIG.webHook();
  public static final String TOKEN = CONFIG.token();
}
