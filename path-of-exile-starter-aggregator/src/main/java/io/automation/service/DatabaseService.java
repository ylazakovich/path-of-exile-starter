package io.automation.service;

import io.automation.dto.SkillGemDTO;
import io.automation.model.Lines;
import io.automation.model.SkillGem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DatabaseService {

  private final GemService gemService;
  private final PoeNinjaService poeNinjaService;

  @Autowired
  public DatabaseService(GemService gemService, PoeNinjaService poeNinjaService) {
    this.gemService = gemService;
    this.poeNinjaService = poeNinjaService;
  }

  public void loadGems() {
    gemService.deleteAll();
    Mono<ResponseEntity<Lines<SkillGemDTO>>> mono = poeNinjaService.getDataWithGems();
    mono.subscribe(data -> gemService.saveAll(data.getBody()));
  }
}
