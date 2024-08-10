package io.starter.telegram.config;

import org.aeonbits.owner.Config;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;

@LoadPolicy(LoadType.MERGE)
@Sources({"system:env", "classpath:local_resources/bot.properties"})
public interface BotConfiguration extends Config {

  @Key("TELEGRAM_BOT_WEBHOOK")
  String webHook();

  @Key("TELEGRAM_BOT_TOKEN")
  String token();
}
