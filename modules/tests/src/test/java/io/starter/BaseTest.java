package io.starter;

import io.starter.config.AppConfig;
import io.starter.services.a8r.steps.A8rServiceSteps;
import io.starter.services.telegram.steps.RegressionServiceSteps;
import io.starter.ui.telegram.TelegramNavigationSteps;

public abstract class BaseTest {

  protected TelegramNavigationSteps telegramNavigationSteps = new TelegramNavigationSteps(AppConfig.BASE_TELEGRAM_URL);
  protected RegressionServiceSteps regressionServiceSteps = new RegressionServiceSteps(AppConfig.WEBHOOK_URL);
  protected A8rServiceSteps a8rServiceSteps = new A8rServiceSteps(AppConfig.BASE_A8R_URL);
}
