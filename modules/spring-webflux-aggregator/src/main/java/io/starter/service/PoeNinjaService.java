package io.starter.service;

import java.util.Map;

import io.starter.client.AbstractWebClientService;
import io.starter.config.NinjaConfiguration;
import io.starter.model.ninja.Currency;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;
import io.starter.type.CurrencyTypeReference;
import io.starter.type.SkillTypeReference;
import io.starter.type.TypeRefFactory;

import org.aeonbits.owner.ConfigFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PoeNinjaService extends AbstractWebClientService {

  private static final NinjaConfiguration CONFIG =
      ConfigFactory.create(NinjaConfiguration.class, System.getProperties());

  private static final String ITEMS = "/api/data/itemoverview";
  private static final String CURRENCIES = "/api/data/currencyoverview";

  public PoeNinjaService() {
    super(CONFIG.useMockServerAsProxy(), CONFIG.baseUrl(), CONFIG.realUrl());
  }

  public Mono<ResponseEntity<Lines<Skill>>> getSkills(String league) {
    return fetch(ITEMS, league, "SkillGem", SkillTypeReference::new);
  }

  public Mono<ResponseEntity<Lines<Currency>>> getRates(String league) {
    return fetch(CURRENCIES, league, "Currency", CurrencyTypeReference::new);
  }

  private <T> Mono<ResponseEntity<Lines<T>>> fetch(
      String path,
      String league,
      String type,
      TypeRefFactory<T> typeRefFactory
  ) {
    return get(path, Map.of("league", league, "type", type), typeRefFactory.get());
  }
}

