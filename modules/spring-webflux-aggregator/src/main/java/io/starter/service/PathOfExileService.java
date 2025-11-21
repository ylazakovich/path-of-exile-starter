package io.starter.service;

import java.util.Collections;
import java.util.List;

import io.starter.client.AbstractWebClientService;
import io.starter.config.PathOfExileConfiguration;
import io.starter.model.path_of_exile.League;

import org.aeonbits.owner.ConfigFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import tools.jackson.databind.json.JsonMapper;

@Service
public class PathOfExileService extends AbstractWebClientService {

  private static final PathOfExileConfiguration CONFIG = ConfigFactory
      .create(PathOfExileConfiguration.class, System.getProperties());

  private static final String LEAGUES = "/api/leagues";

  public PathOfExileService(JsonMapper jsonMapper) {
    super(jsonMapper, CONFIG.useMockServerAsProxy(), CONFIG.baseUrl(), CONFIG.realUrl());
  }

  public Mono<ResponseEntity<List<League>>> getAllLeagues() {
    return get(
        LEAGUES,
        Collections.emptyMap(),
        Collections.emptyMap(),
        new ParameterizedTypeReference<>() {
        }
    );
  }
}
