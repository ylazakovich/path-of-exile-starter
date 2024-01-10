package io.automation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/health")
public class HealthCheck {

  @GetMapping("/ping")
  public Mono<String> ping() {
    return Mono.just("ok");
  }
}
