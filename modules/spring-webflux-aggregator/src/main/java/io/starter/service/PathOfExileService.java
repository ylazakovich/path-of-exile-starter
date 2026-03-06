package io.starter.service;

import java.util.Collections;
import java.util.List;

import io.starter.client.AbstractWebClientService;
import io.starter.config.PathOfExileConfiguration;
import io.starter.model.path_of_exile.League;

import lombok.extern.log4j.Log4j2;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import tools.jackson.databind.json.JsonMapper;

@Service
@Log4j2
public class PathOfExileService extends AbstractWebClientService {

  private static final PathOfExileConfiguration CONFIG = ConfigFactory
      .create(PathOfExileConfiguration.class, System.getProperties());

  private static final String LEAGUES = "/api/leagues";

  public PathOfExileService(JsonMapper jsonMapper) {
    super(
        jsonMapper,
        CONFIG.useMockServerAsProxy(),
        CONFIG.useMockServerAsFallback(),
        CONFIG.baseUrl(),
        CONFIG.realUrl()
    );
  }

  public Mono<ResponseEntity<List<League>>> getAllLeagues() {
    Mono<ResponseEntity<List<League>>> request = get(
        LEAGUES,
        Collections.emptyMap(),
        Collections.emptyMap(),
        new ParameterizedTypeReference<List<League>>() {
        }
    );
    return request.onErrorResume(error -> {
      log.warn("Unable to load leagues from Path of Exile API. Returning empty list.", error);
      return Mono.just(ResponseEntity.ok(List.<League>of()));
    });
  }
}
