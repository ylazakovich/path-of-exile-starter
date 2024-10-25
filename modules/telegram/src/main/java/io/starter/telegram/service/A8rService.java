package io.starter.telegram.service;

import java.util.List;

import io.starter.telegram.config.third_party.A8rConfig;
import io.starter.telegram.model.aggregator.League;
import io.starter.telegram.model.aggregator.Skill;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class A8rService {

  private static final String SKILLS = "/analyzer/analyze/skills?league=%s";
  private static final String LEAGUES = "/database/leagues";

  private final WebClient client;

  public A8rService() {
    this.client = WebClient.builder()
        .baseUrl(A8rConfig.BASE_URL)
        .build();
  }

  public Mono<List<Skill>> getAnalyzedSkills(String league) {
    return client.get()
        .uri(SKILLS.formatted(league))
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
