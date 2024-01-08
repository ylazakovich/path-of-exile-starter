package io.automation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

  private final SkillGemService skillGemService;
  private final PoeNinjaService poeNinjaService;

  @Autowired
  public DatabaseService(SkillGemService skillGemService,
                         PoeNinjaService poeNinjaService) {
    this.skillGemService = skillGemService;
    this.poeNinjaService = poeNinjaService;
  }

  public void loadGems() {
    skillGemService.deleteAll();
    poeNinjaService.getDataWithGems().subscribe(data -> skillGemService.saveAll(data.getBody()));
  }
}
