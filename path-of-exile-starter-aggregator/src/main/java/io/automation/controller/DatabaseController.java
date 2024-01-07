package io.automation.controller;

import java.util.List;
import java.util.Objects;

import io.automation.dto.SkillGemDTO;
import io.automation.entity.SkillGemEntity;
import io.automation.model.Lines;
import io.automation.model.SkillGem;
import io.automation.service.SkillGemService;
import io.automation.service.PoeNinjaService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/database")
public class DatabaseController {

  private final SkillGemService skillGemService;
  private final PoeNinjaService poeNinjaService;

  public DatabaseController(SkillGemService skillGemService, PoeNinjaService poeNinjaService) {
    this.skillGemService = skillGemService;
    this.poeNinjaService = poeNinjaService;
  }

  @GetMapping("/load/gems")
  public void loadGems() {
    skillGemService.deleteAll();
    Mono<ResponseEntity<Lines<SkillGem>>> dataWithGems = poeNinjaService.getDataWithGems();
    dataWithGems.subscribe(data -> skillGemService.saveAll(data.getBody()));
  }

  @Scheduled(cron = "* */30 * * * *")
  @GetMapping("/update/gems/prices")
  public void updatePricesGems() {
    List<SkillGemEntity> pastState = skillGemService.findAllGems();
    poeNinjaService.getDataWithGems().subscribe(data -> {
      pastState.forEach(pastPrice -> Objects.requireNonNull(data.getBody()).getLines().stream()
          .filter(currentPrice -> currentPrice.getName().equals(pastPrice.getName()) &&
              currentPrice.getVariant().equals(pastPrice.getVariant()))
          .findFirst().ifPresent(matchedEntity -> pastPrice.setChaosValue(matchedEntity.getChaosValue())));
      skillGemService.saveAll(pastState);
    });
  }
}
