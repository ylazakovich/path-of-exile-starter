package io.starter.telegram.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:env", "classpath:local_resources/bot.properties"})
public interface BotConfiguration extends Config {

  @Key("TELEGRAM_BOT_WEBHOOK")
  String webHook();

  @Key("TELEGRAM_BOT_TOKEN")
  String token();
}
