package io.starter.services.telegram;

import io.restassured.response.ValidatableResponse;

public class RegressionService extends TelegramService {

  private static final String CODE = "/regression/code";
  private static final String EXPIRE_CODE = "/regression/code/expire";

  public RegressionService(String baseUrl) {
    super(baseUrl);
  }

  public ValidatableResponse getCode() {
    return get(CODE);
  }

  public ValidatableResponse expireCode() {
    return put(EXPIRE_CODE);
  }
}
