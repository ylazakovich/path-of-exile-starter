package io.starter.config;

import org.aeonbits.owner.ConfigFactory;

public class PathOfExileConfig {

  private static final PathOfExileConfiguration CONFIG =
      ConfigFactory.create(PathOfExileConfiguration.class, System.getProperties());

  public static final String BASE_URL = CONFIG.baseUrl();
}
