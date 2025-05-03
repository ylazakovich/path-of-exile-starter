package io.starter.config;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({"system:env"})
public interface PathOfExileConfiguration extends Config {

  @Key("PATH_OF_EXILE_USE_MOCK_SERVER_AS_PROXY")
  @DefaultValue("false")
  Boolean useMockServerAsProxy();

  @Key("PATH_OF_EXILE_BASE_URL")
  @DefaultValue("https://www.pathofexile.com")
  String baseUrl();
}
