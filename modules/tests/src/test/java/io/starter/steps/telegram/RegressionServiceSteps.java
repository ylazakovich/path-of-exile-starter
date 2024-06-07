package io.starter.steps.telegram;

import java.time.Duration;

import io.qameta.allure.Step;
import io.starter.services.telegram.RegressionService;
import io.starter.utils.Waiter;

public class RegressionServiceSteps {

  private final RegressionService regressionService;

  public RegressionServiceSteps(String baseUrl) {
    this.regressionService = new RegressionService(baseUrl);
  }

  @Step("Get validation code for login in Telegram Account")
  public String getValidationCode() {
    awaitValidationCode();
    return regressionService.getValidatableCode().extract().jsonPath().getString("code");
  }

  private void awaitValidationCode() {
    Waiter.awaitCondition(() -> regressionService.getValidatableCode().extract().jsonPath().getBoolean("is_not_used"),
        "No verification code. Admin can provide it",
        Duration.ofSeconds(60),
        Duration.ofSeconds(4));
  }
}
