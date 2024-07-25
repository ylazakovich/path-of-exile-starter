package io.starter.ui.telegram;

import java.time.Duration;

import io.starter.services.telegram.RegressionService;
import io.starter.utils.Waiter;

import io.qameta.allure.Step;

public class RegressionServiceSteps {

  private final RegressionService regressionService;

  public RegressionServiceSteps(String baseUrl) {
    this.regressionService = new RegressionService(baseUrl);
  }

  @Step("Get validation code for login in Telegram Account")
  public String getCode() {
    awaitValidationCode();
    return regressionService.getCode()
        .extract()
        .jsonPath().getString("code");
  }

  @Step("Get validation code for login in Telegram Account")
  public void expireCode() {
    regressionService.expireCode();
  }

  private void awaitValidationCode() {
    Waiter.awaitCondition(() -> regressionService.getCode().extract().jsonPath().getBoolean("is_not_used"),
        "No verification code. Admin can provide it",
        Duration.ofSeconds(60),
        Duration.ofSeconds(4));
  }
}
