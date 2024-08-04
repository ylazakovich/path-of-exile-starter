package io.starter.telegram.config;

import org.aeonbits.owner.ConfigFactory;

public class AggregatorConfig {

  private static final AggregatorConfiguration CONFIG =
      ConfigFactory.create(AggregatorConfiguration.class, System.getProperties());

  public static final String BASE_URL = CONFIG.baseUrl();
}
