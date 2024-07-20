package io.starter.services.telegram;

import io.starter.services.ApiService;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TelegramService extends ApiService {

  private final String baseUrl;

  protected ValidatableResponse get(String path) {
    return get(getRequestSpecification(ContentType.JSON), path);
  }

  protected ValidatableResponse put(String path) {
    return put(getRequestSpecification(ContentType.JSON), path);
  }

  private RequestSpecification getRequestSpecification(ContentType contentType) {
    return getRequestSpecification(baseUrl, contentType);
  }
}
