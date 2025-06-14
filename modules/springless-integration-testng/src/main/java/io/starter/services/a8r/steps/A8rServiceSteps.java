package io.starter.services.a8r.steps;

import io.starter.services.a8r.A8rHealthService;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class A8rServiceSteps {

  private A8rHealthService a8rHealthService;

  public A8rServiceSteps(String baseUrl) {
    this.a8rHealthService = new A8rHealthService(baseUrl);
  }

  @Step("GET /health/ping")
  public ValidatableResponse getPingResponse() {
    return a8rHealthService.getPingResponse();
  }
}
