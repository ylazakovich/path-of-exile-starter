package io.starter.services.telegram;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.starter.services.ApiService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TelegramService extends ApiService {

  private final String baseUrl;

  protected ValidatableResponse get(String path) {
    return get(getRequestSpecification(ContentType.JSON), path);
  }

  private RequestSpecification getRequestSpecification(ContentType contentType) {
    return getRequestSpecification(baseUrl, contentType);
  }
}
