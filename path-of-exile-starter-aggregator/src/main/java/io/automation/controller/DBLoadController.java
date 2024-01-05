package io.automation.controller;

import java.util.List;

import io.automation.dto.GemDTO;
import io.automation.entity.GemEntity;
import io.automation.service.GemService;
import io.automation.service.PoeNinjaService;
import org.springframework.scheduling.annotation.Scheduled;
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

  @Scheduled(cron = "* */30 * * * *")
  @GetMapping("/update/gems/prices")
  public void updatePricesGems() {
    List<GemEntity> pastState = gemService.findAllGems();
    Mono<GemDTO> currentState = poeNinjaService.getDataWithGems();
    currentState.subscribe(dto -> {
      pastState
          .forEach(pastPrice -> GemDTO.convertToEntity(dto.getLines()).stream()
              .filter(currentPrice -> currentPrice.getName().equals(pastPrice.getName())
                  && currentPrice.getVariant().equals(pastPrice.getVariant()))
              .findFirst().ifPresent(matchedEntity -> pastPrice.setChaosValue(matchedEntity.getChaosValue())));
      gemService.saveAll(pastState);
    });
  }
}
