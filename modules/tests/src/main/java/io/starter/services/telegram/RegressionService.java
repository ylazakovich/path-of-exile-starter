package io.starter.services.telegram;

import io.restassured.response.ValidatableResponse;

public class RegressionService extends TelegramService {

  private static final String CODE = "/regression/code";

  public RegressionService(String baseUrl) {
    super(baseUrl);
  }

  public ValidatableResponse getValidatableCode() {
    return get(CODE);
  }
}
