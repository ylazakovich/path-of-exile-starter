package io.starter.services;

import java.util.concurrent.TimeUnit;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;

public class ApiService {

  protected ValidatableResponse get(RequestSpecification requestSpecification, String path) {
    return given(requestSpecification)
        .when()
        .get(path)
        .then()
        .spec(getResponseSpecification());
  }

  protected RequestSpecification getRequestSpecification(String uri, ContentType contentType) {
    return new RequestSpecBuilder()
        .setBaseUri(uri)
        .setContentType(contentType)
        .build();
  }

  protected ResponseSpecification getResponseSpecification() {
    return new ResponseSpecBuilder()
        .expectResponseTime(Matchers.lessThan(60L), TimeUnit.SECONDS)
        .build();
  }
}
