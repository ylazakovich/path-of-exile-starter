package io.starter.telegram.config;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;

import org.aeonbits.owner.Config;

@LoadPolicy(LoadType.MERGE)
@Sources({"system:env"})
public interface AggregatorConfiguration extends Config {

  @Key("AGGREGATOR_BASE_URL")
  @DefaultValue("http://localhost:8080")
  String baseUrl();
}
