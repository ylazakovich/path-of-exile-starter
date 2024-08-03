package io.starter.controller;

import java.util.Objects;

import io.starter.entity.LeagueEntity;
import io.starter.service.DatabaseNinjaService;
import io.starter.service.DatabasePathOfExileService;
import io.starter.service.PathOfExileService;
import io.starter.service.PoeNinjaService;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/database")
@Log4j2
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

  @PostMapping("/load/skills")
  public void loadAllSkills() {
    log.info("Started process with loading all skills...");
    databasePathOfExileService.readAll().forEach(this::loadSkills);
    log.info("Finished process with loading all skills");
  }

  private void loadSkills(LeagueEntity league) {
    log.info("Started process with loading skills from league - {}", league.getName());
    poeNinjaService.getSkills(league.getName()).subscribe(data -> databaseNinjaService.load(data.getBody(), league));
    log.info("Finished process with loading skills from league - {}", league);
  }

  @PostMapping("/load/leagues")
  public void loadAllLeagues() {
    log.info("Started process with loading all leagues...");
    pathOfExileService.getAllLeagues().subscribe(data -> databasePathOfExileService.load(data.getBody()));
    log.info("Finished process with loading all leagues");
  }

  @Scheduled(cron = "0 */5 * * * *")
  public void updateSkills() {
    log.info("Started process with updating all skills...");
    databasePathOfExileService.readAll()
        .forEach(league -> poeNinjaService.getSkills(league.getName())
            .subscribe(data -> databaseNinjaService.update(data.getBody(), league)));
    log.info("Finished process with updating all skills");
  }

  @Scheduled(cron = "0 */2 * * * *")
  public void addNewSkills() {
    log.info("Started process with adding new skills...");
    databasePathOfExileService.readAll()
        .forEach(league -> poeNinjaService.getSkills(league.getName())
            .subscribe(data -> databaseNinjaService.addNew(Objects.requireNonNull(data.getBody()), league)));
    log.info("Finished process with adding new skills");
  }
}
