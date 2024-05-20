package io.starter.config;

import org.aeonbits.owner.Config;

public interface BrowserConfiguration extends Config {

  @Key("BROWSER")
  @DefaultValue("chrome")
  String browser();

  @Key("REMOTE_URL")
  String remoteUrl();

}
