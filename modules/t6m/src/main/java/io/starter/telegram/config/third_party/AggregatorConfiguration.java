package io.starter.telegram.config.third_party;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({"system:env"})
public interface AggregatorConfiguration extends Config {

  @Key("AGGREGATOR_BASE_URL")
  @DefaultValue("http://localhost:8080")
  String baseUrl();
}
