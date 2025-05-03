package io.starter.service;

import io.starter.config.NinjaConfiguration;
import io.starter.model.ninja.Currency;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;
import io.starter.shared.AbstractWebClientService;

import org.aeonbits.owner.ConfigFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PoeNinjaService extends AbstractWebClientService {

  private static final NinjaConfiguration CONFIG =
      ConfigFactory.create(NinjaConfiguration.class, System.getProperties());

  public PoeNinjaService() {
    super(CONFIG.useMockServerAsProxy(), CONFIG.baseUrl());
  }

  public Mono<ResponseEntity<Lines<Skill>>> getSkills(String league) {
    return client.get()
        .uri("%s?%s".formatted(CONFIG.itemRoute(), "league=%s&type=SkillGem".formatted(league)))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<>() {
        });
  }

  public Mono<ResponseEntity<Lines<Currency>>> getRates(String league) {
    return client.get()
        .uri("%s?%s".formatted(CONFIG.currencyRoute(), "league=%s&type=Currency".formatted(league)))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .toEntity(new ParameterizedTypeReference<>() {
        });
  }
}

