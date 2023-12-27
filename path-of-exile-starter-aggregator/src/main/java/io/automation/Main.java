package io.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

  /*
  1) TODO: database after reload data id starts not from 1
  2) TODO: aggregate gems and output profitable goods
  3) TODO: optimize build gradle
  4) TODO: build image of service
  5) TODO: bash script for full service aggregator
  6) TODO: Controller investigate how to return correct ResponseEntity from Mono/Flex
  7) TODO: Start telegram integration
   */
  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}