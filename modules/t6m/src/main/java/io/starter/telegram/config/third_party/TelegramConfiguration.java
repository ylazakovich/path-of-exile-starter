package io.starter.telegram.config.third_party;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({"system:env"})
public interface TelegramConfiguration extends Config {

  @Key("TELEGRAM_BASE_URL")
  @DefaultValue("https://api.telegram.org")
  String baseUrl();
}
