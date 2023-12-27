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

  private final PoeNinjaConfig CONFIG;
  private final WebClient CLIENT;

  public PoeNinjaService() {
    this.CONFIG = new PoeNinjaConfig();
    this.CLIENT = WebClient.builder()
        .baseUrl(CONFIG.BASE_URL)
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
    return CLIENT.get()
        .uri("%s?%s".formatted(CONFIG.ROUTE, "league=Ancestor&type=SkillGem"))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(GemDTO.class);
  }
}
