package io.starter.service;

import java.util.List;

import io.starter.config.PathOfExileConfig;
import io.starter.model.path_of_exile.League;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PathOfExileService {

  private final static String LEAGUES = "api/leagues";
  private final WebClient client;

  public PathOfExileService() {
    this.client = WebClient.builder()
        .baseUrl(PathOfExileConfig.BASE_URL)
        .build();
  }

  public Mono<ResponseEntity<List<League>>> getAllLeagues() {
    return client.get()
        .uri(LEAGUES)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .toEntityList(League.class);
  }
}
