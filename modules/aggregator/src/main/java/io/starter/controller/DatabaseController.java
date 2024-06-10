package io.starter.controller;

import java.util.Objects;

import io.starter.service.DatabaseService;
import io.starter.service.PoeNinjaService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database")
public class DatabaseController {

  private final DatabaseService databaseService;
  private final PoeNinjaService poeNinjaService;

  public DatabaseController(DatabaseService databaseService,
                            PoeNinjaService poeNinjaService) {
    this.databaseService = databaseService;
    this.poeNinjaService = poeNinjaService;
  }

  @GetMapping("/load/skills")
  public void loadSkills() {
    poeNinjaService.getSkills().subscribe(data -> databaseService.load(Objects.requireNonNull(data.getBody())));
  }

  @Scheduled(cron = "0 */5 * * * *")
  public void updateSkills() {
    poeNinjaService.getSkills().subscribe(data -> databaseService.update(Objects.requireNonNull(data.getBody())));
  }

  @Scheduled(cron = "0 */2 * * * *")
  public void addNewSkills() {
    poeNinjaService.getSkills().subscribe(data -> databaseService.addNew(Objects.requireNonNull(data.getBody())));
  }
}
