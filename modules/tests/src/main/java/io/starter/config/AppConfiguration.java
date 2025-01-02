package io.starter.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({"system:env"})
public interface AppConfiguration extends Config {

  @Key("BASE_TELEGRAM_URL")
  @DefaultValue("https://web.telegram.org")
  String telegramBaseUrl();

  @Key("A8R_BASE_URL")
  @DefaultValue("http://localhost:8080")
  String a8rBaseUrl();

  @Key("ACCOUNT_PHONE")
  String accountPhone();

  @Key("WEBHOOK_URL")
  String webhookUrl();

  @Key("BOT_USERNAME")
  @DefaultValue("@poe_consultant_bot")
  String botUsername();

  @Key("IS_NIGHTLY_RUN")
  @DefaultValue("false")
  Boolean isNightlyRun();
}
