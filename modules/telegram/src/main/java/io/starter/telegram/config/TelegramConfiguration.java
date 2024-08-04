package io.starter.telegram.config;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;

import org.aeonbits.owner.Config;

@LoadPolicy(LoadType.MERGE)
@Sources({"system:env"})
public interface TelegramConfiguration extends Config {

  @Key("TELEGRAM_BASE_URL")
  @DefaultValue("https://api.telegram.org")
  String baseUrl();
}
