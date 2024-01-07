package io.automation.service;

import io.automation.model.Lines;
import io.automation.model.SkillGem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DatabaseService {

  private final SkillGemService skillGemService;
  private final PoeNinjaService poeNinjaService;

  @Autowired
  public DatabaseService(SkillGemService skillGemService, PoeNinjaService poeNinjaService) {
    this.skillGemService = skillGemService;
    this.poeNinjaService = poeNinjaService;
  }

  public void loadGems() {
    skillGemService.deleteAll();
    Mono<ResponseEntity<Lines<SkillGem>>> mono = poeNinjaService.getDataWithGems();
    mono.subscribe(data -> skillGemService.saveAll(data.getBody()));
  }
}
