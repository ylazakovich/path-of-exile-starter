package io.starter.services.a8r;

import io.restassured.response.ValidatableResponse;

public class A8rHealthService extends A8rService {

  private static final String BASE = "/health";
  private static final String PING = BASE + "/ping";

  public A8rHealthService(String baseUrl) {
    super(baseUrl);
  }

  public ValidatableResponse getPingResponse() {
    return get(PING);
  }
}
