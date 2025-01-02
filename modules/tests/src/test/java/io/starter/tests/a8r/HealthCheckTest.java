package io.starter.tests.a8r;

import io.starter.BaseTest;

import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

public class HealthCheckTest extends BaseTest {

  @Test(description = "Spring service 'A8r' should be healthy")
  void testA8rHealthyState() {
    ValidatableResponse response = a8rServiceSteps.getPingResponse();
    response.statusCode(200);
  }
}
