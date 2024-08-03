package io.starter.service;

import io.starter.config.NinjaConfig;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PoeNinjaService {

  private final WebClient client;

  public PoeNinjaService() {
    this.client = WebClient.builder()
        .baseUrl(NinjaConfig.BASE_URL)
        .exchangeStrategies(ExchangeStrategies
            .builder()
            .codecs(
                codecs -> codecs
                    .defaultCodecs()
                    .maxInMemorySize(8 * 100 * 1024 * 1024))
            .build())
        .build();
  }

  public Mono<ResponseEntity<Lines<Skill>>> getSkills(String league) {
    return client.get()
        .uri("%s?%s".formatted(NinjaConfig.ROUTE, "league=%s&type=SkillGem".formatted(league)))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<>() {
        });
  }
}

