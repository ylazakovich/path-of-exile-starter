package io.starter.controller;

import io.starter.service.DatabaseService;
import io.starter.service.PoeNinjaService;
import io.starter.service.SkillsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database")
public class DatabaseController {

  private final DatabaseService databaseService;
  private final SkillsService skillsService;
  private final PoeNinjaService poeNinjaService;

  public DatabaseController(DatabaseService databaseService,
                            SkillsService skillsService,
                            PoeNinjaService poeNinjaService) {
    this.databaseService = databaseService;
    this.skillsService = skillsService;
    this.poeNinjaService = poeNinjaService;
  }

  @GetMapping("/load/skills")
  public void loadSkills() {
    databaseService.loadSkills();
  }

  @Scheduled(cron = "* */10 * * * *")
  @GetMapping("/update/skills/prices")
  public void updatePricesGems() {
    databaseService.refreshSkills();
  }
}
