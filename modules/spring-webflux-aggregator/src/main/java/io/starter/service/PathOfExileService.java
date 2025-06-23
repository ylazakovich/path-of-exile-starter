package io.starter.service;

import java.util.List;
import java.util.Map;

import io.starter.client.AbstractWebClientService;
import io.starter.config.PathOfExileConfiguration;
import io.starter.model.path_of_exile.League;

import org.aeonbits.owner.ConfigFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PathOfExileService extends AbstractWebClientService {

  private static final PathOfExileConfiguration CONFIG = ConfigFactory
      .create(PathOfExileConfiguration.class, System.getProperties());

  private static final String LEAGUES = "api/leagues";

  public PathOfExileService() {
    super(CONFIG.useMockServerAsProxy(), CONFIG.baseUrl(), CONFIG.realUrl());

  }

  public Mono<ResponseEntity<List<League>>> getAllLeagues() {
    return get(
        LEAGUES,
        Map.of(),
        Map.of(),
        new ParameterizedTypeReference<>() {
        }
    );
  }
}
