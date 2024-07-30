package io.starter.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:env"})
public interface NinjaConfiguration extends Config {

  @Config.Key("NINJA_BASE_URL")
  @Config.DefaultValue("https://poe.ninja")
  String baseUrl();

  @Config.Key("NINJA_ROUTE")
  @Config.DefaultValue("/api/data/itemoverview")
  String route();

  @Config.Key("NINJA_LEAGUE")
  @Config.DefaultValue("Standard")
  String league();
}
