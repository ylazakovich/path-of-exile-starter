package io.starter.service;

import java.util.Map;

import io.starter.client.AbstractWebClientService;
import io.starter.config.NinjaConfiguration;
import io.starter.model.ninja.Currency;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;

import org.aeonbits.owner.ConfigFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PoeNinjaService extends AbstractWebClientService {

  private static final NinjaConfiguration CONFIG =
      ConfigFactory.create(NinjaConfiguration.class, System.getProperties());

  public PoeNinjaService() {
    super(CONFIG.useMockServerAsProxy(), CONFIG.baseUrl(), CONFIG.realUrl());
  }

  public Mono<ResponseEntity<Lines<Skill>>> getSkills(String league) {
    return get(
        CONFIG.itemRoute(),
        Map.of("league", league, "type", "SkillGem"),
        Map.of(),
        new ParameterizedTypeReference<>() {
        }
    );
  }

  public Mono<ResponseEntity<Lines<Currency>>> getRates(String league) {
    return get(
        CONFIG.currencyRoute(),
        Map.of("league", league, "type", "Currency"),
        Map.of(),
        new ParameterizedTypeReference<>() {
        }
    );
  }
}

