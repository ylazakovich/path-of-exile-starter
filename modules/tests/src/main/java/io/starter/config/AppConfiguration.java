package io.starter.config;

import org.aeonbits.owner.Config;

public interface AppConfiguration extends Config {

  @Key("BASE_TELEGRAM_URL")
  @DefaultValue("https://web.telegram.org")
  String baseTelegramUrl();

  @Key("ACCOUNT_PHONE")
  String accountPhone();

  @Key("WEBHOOK_URL")
  String webhookUrl();

  @Key("BOT_USERNAME")
  @DefaultValue("@poe_consultant_bot")
  String botUsername();
}
