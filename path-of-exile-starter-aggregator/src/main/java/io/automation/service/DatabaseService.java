package io.automation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

  private final SkillsService skillsService;
  private final PoeNinjaService poeNinjaService;

  @Autowired
  public DatabaseService(SkillsService skillsService,
                         PoeNinjaService poeNinjaService) {
    this.skillsService = skillsService;
    this.poeNinjaService = poeNinjaService;
  }

  public void loadSkills() {
    skillsService.clearTable();
    poeNinjaService.getSkills().subscribe(data -> skillsService.saveAll(data.getBody()));
  }
}
