package io.automation.controller;

import java.util.List;
import java.util.stream.Collectors;

import io.automation.dto.GemDTO;
import io.automation.entity.GemEntity;
import io.automation.service.GemService;
import io.automation.service.PoeNinjaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/gem")
public class GemResource {

  private final GemService gemService;
  private final PoeNinjaService poeNinjaService;

  public GemResource(GemService gemService, PoeNinjaService client) {
    this.gemService = gemService;
    this.poeNinjaService = client;
  }

  @GetMapping("/load")
  public void loadDataToDatabase() {
    gemService.deleteAll();
    Mono<GemDTO> dataWithGems = poeNinjaService.getDataWithGems();
    dataWithGems.subscribe(body -> gemService.saveAll(body));
  }

  @GetMapping("/all")
  public List<GemDTO.Gem> getAllGems() {
    List<GemEntity> entities = gemService.findAllGems();
    return new GemDTO(entities).getLines();
  }
}
