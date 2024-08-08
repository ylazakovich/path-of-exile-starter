package io.starter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

  @GetMapping("/ping")
  public Mono<String> ping() {
    // TODO: here need to write logic with checking that tables are not empty and step by step 
    return Mono.just("ok");
  }
}
