package io.starter.tests.a8r;

import io.starter.BaseTest;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

@Epic("Service Aggregator")
public class HealthCheckTest extends BaseTest {

  @Feature("GET /health/ping")
  @Test(description = "Spring service 'A8r' should be healthy")
  void testA8rHealthyState() {
    ValidatableResponse response = a8rServiceSteps.getPingResponse();
    response.statusCode(200);
  }
}
