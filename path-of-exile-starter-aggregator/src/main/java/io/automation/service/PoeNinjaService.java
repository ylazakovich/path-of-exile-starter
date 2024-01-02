package io.automation.service;

import io.automation.config.PoeNinjaConfig;
import io.automation.dto.GemDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PoeNinjaService {

  private final PoeNinjaConfig config;
  private final WebClient client;

  public PoeNinjaService() {
    this.config = new PoeNinjaConfig();
    this.client = WebClient.builder()
        .baseUrl(config.baseUrl)
        .exchangeStrategies(ExchangeStrategies
            .builder()
            .codecs(
                codecs -> codecs
                    .defaultCodecs()
                    .maxInMemorySize(8 * 100 * 1024 * 1024))
            .build())
        .build();
  }

  public Mono<GemDTO> getDataWithGems() {
    return client.get()
        .uri("%s?%s".formatted(config.route, "league=%s&type=SkillGem".formatted(config.league)))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(GemDTO.class);
  }
}
