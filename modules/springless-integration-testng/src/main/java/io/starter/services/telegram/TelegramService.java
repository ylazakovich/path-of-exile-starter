package io.starter.services.telegram;

import io.starter.services.ApiService;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TelegramService extends ApiService {

  private final String baseUrl;

  protected ValidatableResponse get(String path) {
    return get(getRequestSpecification(), path);
  }

  protected ValidatableResponse put(String path) {
    return put(getRequestSpecification(), path);
  }

  private RequestSpecification getRequestSpecification() {
    return getRequestSpecification(baseUrl);
  }
}
