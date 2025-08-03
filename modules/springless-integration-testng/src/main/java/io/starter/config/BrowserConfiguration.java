package io.starter.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:env"})
public interface BrowserConfiguration extends Config {

  @Key("BROWSER")
  @DefaultValue("chrome")
  String browser();

  @Key("REMOTE_URL")
  String remoteUrl();

  @Key("CI")
  @DefaultValue("false")
  Boolean isRemote();
}
