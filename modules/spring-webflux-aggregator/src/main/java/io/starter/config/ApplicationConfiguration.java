package io.starter.config;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({"system:env", "classpath:local_resources/application.properties"})
public interface ApplicationConfiguration extends Config {

  @Key("START_UP_LOADER_ENABLED")
  @DefaultValue("true")
  Boolean startupLoaderEnabled();
}
