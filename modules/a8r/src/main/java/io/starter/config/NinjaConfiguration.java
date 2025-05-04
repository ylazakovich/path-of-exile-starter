package io.starter.config;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({"system:env", "classpath:local_resources/application.properties"})
public interface NinjaConfiguration extends Config {

  @Key("NINJA_BASE_URL")
  @DefaultValue("https://poe.ninja")
  String baseUrl();

  @Key("NINJA_REAL_URL")
  @DefaultValue("https://www.pathofexile.com")
  String realUrl();

  @Key("NINJA_ITEM_ROUTE")
  @DefaultValue("/api/data/itemoverview")
  String itemRoute();

  @Key("NINJA_CURRENCY_ROUTE")
  @DefaultValue("/api/data/currencyoverview")
  String currencyRoute();

  @Key("NINJA_USE_MOCK_SERVER_AS_PROXY")
  @DefaultValue("false")
  Boolean useMockServerAsProxy();
}
