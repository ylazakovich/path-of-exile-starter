package io.automation.service;

import io.automation.dto.GemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DBService {

  private final GemService gemService;
  private final PoeNinjaService poeNinjaService;

  @Autowired
  public DBService(GemService gemService, PoeNinjaService poeNinjaService) {
    this.gemService = gemService;
    this.poeNinjaService = poeNinjaService;
  }

  public void loadGems() {
    gemService.deleteAll();
    Mono<GemDTO> dataWithGems = poeNinjaService.getDataWithGems();
    dataWithGems.subscribe(gemService::saveAll);
  }
}
