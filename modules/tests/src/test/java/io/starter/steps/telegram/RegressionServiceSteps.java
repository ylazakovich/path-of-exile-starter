package io.starter.steps.telegram;

import io.qameta.allure.Step;
import io.starter.services.telegram.RegressionService;

public class RegressionServiceSteps {

  private RegressionService regressionService;

  public RegressionServiceSteps(String baseUrl) {
    this.regressionService =  new RegressionService(baseUrl);
  }

  @Step("Get validation code for login in Telegram Account")
  public String getValidationCode() {
    return regressionService.getValidatableCode().extract().jsonPath().getString("code");
  }
}
