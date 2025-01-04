package io.starter;

import io.starter.config.AppConfig;
import io.starter.services.a8r.steps.A8rServiceSteps;
import io.starter.services.telegram.steps.RegressionServiceSteps;

public abstract class BaseTest {

  protected RegressionServiceSteps regressionServiceSteps = new RegressionServiceSteps(AppConfig.WEBHOOK_URL);
  protected A8rServiceSteps a8rServiceSteps = new A8rServiceSteps(AppConfig.A8R_BASE_URL);
}
