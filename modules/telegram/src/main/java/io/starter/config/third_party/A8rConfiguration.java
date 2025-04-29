package io.starter.config.third_party;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({"system:env"})
public interface A8rConfiguration extends Config {

  @Key("A8R_BASE_URL")
  @DefaultValue("http://localhost:8080")
  String baseUrl();
}
