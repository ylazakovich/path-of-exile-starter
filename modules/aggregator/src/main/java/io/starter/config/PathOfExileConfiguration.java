package io.starter.config;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;

import org.aeonbits.owner.Config;

@LoadPolicy(LoadType.MERGE)
@Sources({"system:env"})
public interface PathOfExileConfiguration extends Config {

  @Key("PATH_OF_EXILE__BASE_URL")
  @DefaultValue("https://www.pathofexile.com")
  String baseUrl();
}
