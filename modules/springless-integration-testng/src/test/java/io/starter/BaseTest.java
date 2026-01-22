package io.starter;

import io.starter.config.AppConfig;
import io.starter.services.a8r.steps.A8rServiceSteps;

public abstract class BaseTest {

  protected A8rServiceSteps a8rServiceSteps = new A8rServiceSteps(AppConfig.A8R_BASE_URL);
}
