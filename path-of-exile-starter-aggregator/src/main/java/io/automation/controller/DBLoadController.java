package io.automation.controller;

import io.automation.dto.GemDTO;
import io.automation.service.GemService;
import io.automation.service.PoeNinjaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/database")
public class DBLoadController {

  private final GemService gemService;
  private final PoeNinjaService poeNinjaService;

  public DBLoadController(GemService gemService, PoeNinjaService poeNinjaService) {
    this.gemService = gemService;
    this.poeNinjaService = poeNinjaService;
  }

  @GetMapping("/load/gems")
  public void loadGems() {
    gemService.deleteAll();
    Mono<GemDTO> dataWithGems = poeNinjaService.getDataWithGems();
    dataWithGems.subscribe(gemService::saveAll);
  }

  // TODO: refresh endpoint here;
}
