package io.starter.controller;

import io.starter.component.StartupReadiness;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

  private final StartupReadiness startupReadiness;

  public HealthCheckController(StartupReadiness startupReadiness) {
    this.startupReadiness = startupReadiness;
  }

  @GetMapping("/ping")
  public Mono<ResponseEntity<String>> ping() {
    // TODO: here need to write logic with checking that tables are not empty and step by step
    if (!startupReadiness.isReady()) {
      return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("starting"));
    }
    return Mono.just(ResponseEntity.ok("ok"));
  }
}
