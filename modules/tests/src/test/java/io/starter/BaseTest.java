package io.starter;

import io.starter.config.AppConfig;
import io.starter.steps.telegram.RegressionServiceSteps;
import io.starter.steps.telegram.TelegramNavigationSteps;

abstract class BaseTest {

  protected TelegramNavigationSteps telegramNavigationSteps = new TelegramNavigationSteps(AppConfig.BASE_TELEGRAM_URL);
  protected RegressionServiceSteps regressionServiceSteps = new RegressionServiceSteps(AppConfig.WEBHOOK_URL);
}
