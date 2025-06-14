package io.starter.services.a8r;

import io.starter.services.ApiService;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class A8rService extends ApiService {

  private final String baseUrl;

  protected ValidatableResponse get(String path) {
    return get(getRequestSpecification(), path);
  }

  private RequestSpecification getRequestSpecification() {
    return getRequestSpecification(baseUrl);
  }

}
