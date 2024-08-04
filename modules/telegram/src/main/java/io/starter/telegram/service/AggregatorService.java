package io.starter.telegram.service;

import java.util.List;

import io.starter.telegram.config.third_party.AggregatorConfig;
import io.starter.telegram.model.aggregator.League;
import io.starter.telegram.model.aggregator.Skill;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AggregatorService {

  private static final String SKILLS = "/analyzer/analyze/skills";
  private static final String LEAGUES = "/database/leagues";

  private final WebClient client;

  public AggregatorService() {
    this.client = WebClient.builder().baseUrl(AggregatorConfig.BASE_URL).build();
  }

  public Mono<List<Skill>> getAnalyzedSkills() {
    return client.get()
        .uri(SKILLS)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<>() {
        });
  }

  public Mono<List<League>> getLeagues() {
    return client.get()
        .uri(LEAGUES)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<>() {
        });
  }
}
