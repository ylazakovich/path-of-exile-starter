package io.automation.controller;

import java.util.List;

import io.automation.dto.GemDTO;
import io.automation.entity.GemEntity;
import io.automation.service.GemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gems")
public class GemController {

  private final GemService gemService;

  public GemController(GemService gemService) {
    this.gemService = gemService;
  }

  @GetMapping("/findAll")
  public List<GemDTO.Gem> findAll() {
    List<GemEntity> entities = gemService.findAllGems();
    return GemDTO.convertToGem(entities);
  }

  @GetMapping("/findAll/{value}")
  // TODO: find the way how to optimize method;
  public List<GemDTO.Gem> findAllWithProfitMoreThan(@PathVariable("value") long value) {
    List<GemDTO.Gem> data = GemDTO.convertToGem(gemService.findAllGems());
    List<GemDTO.Gem> maxLevelGems = data.stream()
        .filter(gem -> gem.getVariant().equals("20") && !gem.isCorrupted())
        .toList();
    List<GemDTO.Gem> maxQualityGems = data.stream()
        .filter(gem -> gem.getVariant().equals("1/20") && !gem.isCorrupted())
        .toList();
    return maxLevelGems.stream()
        .filter(gem -> maxQualityGems.stream()
            .anyMatch(quality -> gem.getName().equals(quality.getName())
                && (quality.getChaosValue() - gem.getChaosValue() >= value)))
        .toList();
  }
}
