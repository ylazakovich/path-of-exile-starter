package io.starter.controller;

import java.util.Objects;

import io.starter.service.DatabaseNinjaService;
import io.starter.service.DatabasePathOfExileService;
import io.starter.service.PathOfExileService;
import io.starter.service.PoeNinjaService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database")
public class DatabaseController {

  private final DatabasePathOfExileService databasePathOfExileService;
  private final DatabaseNinjaService databaseNinjaService;
  private final PoeNinjaService poeNinjaService;
  private final PathOfExileService pathOfExileService;

  public DatabaseController(DatabasePathOfExileService databasePathOfExileService,
                            DatabaseNinjaService databaseNinjaService,
                            PoeNinjaService poeNinjaService,
                            PathOfExileService pathOfExileService) {
    this.databasePathOfExileService = databasePathOfExileService;
    this.databaseNinjaService = databaseNinjaService;
    this.poeNinjaService = poeNinjaService;
    this.pathOfExileService = pathOfExileService;
  }

  @GetMapping("/load/skills")
  public void loadSkills() {
    poeNinjaService.getSkills().subscribe(data -> databaseNinjaService.load(Objects.requireNonNull(data.getBody())));
  }

  @GetMapping("/load/leagues")
  public void loadAllLeagues() {
    pathOfExileService.getAllLeagues().subscribe(data -> databasePathOfExileService.load(data.getBody()));
  }

  @Scheduled(cron = "0 */5 * * * *")
  public void updateSkills() {
    poeNinjaService.getSkills().subscribe(data -> databaseNinjaService.update(Objects.requireNonNull(data.getBody())));
  }

  @Scheduled(cron = "0 */2 * * * *")
  public void addNewSkills() {
    poeNinjaService.getSkills().subscribe(data -> databaseNinjaService.addNew(Objects.requireNonNull(data.getBody())));
  }
}
