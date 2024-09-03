package io.starter.config;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({"system:env"})
public interface NinjaConfiguration extends Config {

  @Key("NINJA_BASE_URL")
  @DefaultValue("https://poe.ninja")
  String baseUrl();

  @Key("NINJA_ITEM_ROUTE")
  @DefaultValue("/api/data/itemoverview")
  String itemRoute();

  @Key("NINJA_CURRENCY_ROUTE")
  @DefaultValue("/api/data/currencyoverview")
  String currencyRoute();
}
