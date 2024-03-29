package io.starter.telegram.service;

import java.util.List;

import io.starter.aggregator.model.Skill;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AggregatorService {

  private final WebClient client;

  public AggregatorService() {
    this.client = WebClient.builder().baseUrl("http://localhost:8080").build();
  }

  public Mono<List<Skill>> getAnalyzedSkills() {
    return client.get()
        .uri("/analyzer/analyze/skills")
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<>() {
        });
  }
}
