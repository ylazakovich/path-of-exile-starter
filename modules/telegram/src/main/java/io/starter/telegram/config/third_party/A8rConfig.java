package io.starter.telegram.config.third_party;

import org.aeonbits.owner.ConfigFactory;

public class A8rConfig {

  private static final A8rConfiguration CONFIG =
      ConfigFactory.create(A8rConfiguration.class, System.getProperties());

  public static final String BASE_URL = CONFIG.baseUrl();
}
