package io.starter.config;

import org.aeonbits.owner.ConfigFactory;

public class NinjaConfig {

  private static final NinjaConfiguration CONFIG =
      ConfigFactory.create(NinjaConfiguration.class, System.getProperties());

  public static final String BASE_URL = CONFIG.baseUrl();
  public static final String ITEM_ROUTE = CONFIG.itemRoute();
  public static final String CURRENCY_ROUTE = CONFIG.currencyRoute();
}
