package io.starter.telegram.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:env"})
public interface BotConfiguration extends Config {

  @Key("WEBHOOK")
  @DefaultValue("TODO")
  String webHook();

  @Key("TELEGRAM_BOT_TOKEN")
  @DefaultValue("TODO")
  String token();
}
