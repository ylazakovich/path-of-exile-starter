package io.starter.telegram.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:env"})
public interface BotConfiguration extends Config {

  @Key("WEBHOOK")
  @DefaultValue("https://983b-116-202-83-178.ngrok-free.app")
  String webHook();

  @Key("TELEGRAM_BOT_TOKEN")
  @DefaultValue("6983431014:AAF388cP7zOXc-Z1a3B3oO9qzaIrM5qRzbI")
  String token();
}
