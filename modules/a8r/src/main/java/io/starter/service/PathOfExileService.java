package io.starter.service;

import java.util.List;

import io.starter.config.PathOfExileConfiguration;
import io.starter.model.path_of_exile.League;
import io.starter.shared.AbstractWebClientService;

import org.aeonbits.owner.ConfigFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PathOfExileService extends AbstractWebClientService {

  private static final PathOfExileConfiguration CONFIG = ConfigFactory
      .create(PathOfExileConfiguration.class, System.getProperties());

  private static final String LEAGUES = "api/leagues";

  public PathOfExileService() {
    super(CONFIG.useMockServerAsProxy(), CONFIG.baseUrl());

  }

  public Mono<ResponseEntity<List<League>>> getAllLeagues() {
    return client.get()
        .uri(LEAGUES)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .toEntityList(League.class);
  }
}
