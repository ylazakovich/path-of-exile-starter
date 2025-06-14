package io.starter.config;

import org.aeonbits.owner.ConfigFactory;

public class BrowserConfig {

  private static final BrowserConfiguration CONFIG =
      ConfigFactory.create(BrowserConfiguration.class, System.getProperties());

  public static final String BROWSER = CONFIG.browser();
  public static final String REMOTE_URL = CONFIG.remoteUrl();
  public static final Boolean IS_REMOTE = CONFIG.isRemote();
}
