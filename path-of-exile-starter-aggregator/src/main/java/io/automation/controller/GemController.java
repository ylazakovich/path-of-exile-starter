package io.automation.controller;

import java.util.List;

import io.automation.dto.GemDTO;
import io.automation.entity.GemEntity;
import io.automation.service.GemService;
import org.springframework.web.bind.annotation.GetMapping;
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
}
