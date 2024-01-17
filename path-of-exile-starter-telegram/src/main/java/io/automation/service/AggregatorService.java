package io.automation.service;

import java.util.List;

import io.automation.aggregator.model.Skill;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AggregatorService {

  private final WebClient client;

  public AggregatorService() {
    this.client = WebClient.builder().build();
  }

  public Mono<ResponseEntity<List<Skill>>> getAnalyzedSkills() {
    return client.get()
        .uri("/analyzer/analyze/skills")
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<>() {
        });
  }
}
