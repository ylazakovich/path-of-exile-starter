package io.starter.config;

import org.aeonbits.owner.Config;

public interface BrowserConfiguration extends Config {

  @Key("BROWSER")
  @DefaultValue("chrome")
  String browser();

  @Key("REMOTE_URL")
  @DefaultValue("http:// localhost:5678/wd/hub")
  String remoteUrl();

}
