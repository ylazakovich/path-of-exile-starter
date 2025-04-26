package io.starter.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import io.starter.config.ScheduleConfig;
import io.starter.entity.LeagueEntity;
import io.starter.service.DatabaseNinjaService;
import io.starter.service.DatabasePathOfExileService;
import io.starter.service.PathOfExileService;
import io.starter.service.PoeNinjaService;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping("/load/rates")
  public void loadRates() {
    databasePathOfExileService.readAll().forEach(this::loadRates);
  }

  private void loadRates(LeagueEntity league) {
    log.info("Loading rates for: {}", league.getName());
    poeNinjaService.getRates(league.getName())
        .subscribe(data -> databaseNinjaService.loadCurrency(data.getBody(), league));
  }

  @PostMapping("/load/skills")
  public void loadSkills() {
    databasePathOfExileService.readAll().forEach(this::loadSkills);
  }

  private void loadSkills(LeagueEntity league) {
    log.info("Loading skills for: {}", league.getName());
    poeNinjaService.getSkills(league.getName())
        .subscribe(data -> databaseNinjaService.loadSkills(data.getBody(), league));
  }

  @GetMapping ("/leagues")
  public ResponseEntity<List<LeagueEntity>> getLeagues() {
    return ResponseEntity.ok(databasePathOfExileService.readAll());
  }

  @PostMapping("/load/leagues")
  public void loadLeagues() {
    log.info("Loading leagues");
    pathOfExileService.getAllLeagues().subscribe(data -> databasePathOfExileService.load(data.getBody()));
  }

  @Scheduled(cron = ScheduleConfig.A8R_ADD_CRON)
  public void updateRates() {
    databasePathOfExileService.readAll()
        .forEach(league -> poeNinjaService.getRates(league.getName())
            .subscribe(data -> databaseNinjaService.updateCurrencies(data.getBody(), league)));
  }

  @Scheduled(cron = ScheduleConfig.A8R_UPDATE_CRON)
  public void updateSkills() {
    databasePathOfExileService.readAll()
        .forEach(league -> poeNinjaService.getSkills(league.getName())
            .subscribe(data -> databaseNinjaService.updateSkills(data.getBody(), league)));
  }

  @Scheduled(cron = ScheduleConfig.A8R_ADD_CRON)
  public void addNewSkills() {
    databasePathOfExileService.readAll()
        .forEach(league -> poeNinjaService.getSkills(league.getName())
            .subscribe(data -> databaseNinjaService.addNew(Objects.requireNonNull(data.getBody()), league)));
  }

  @SneakyThrows(InterruptedException.class)
  public void loading() {
    Thread.sleep(800);
    loadRates();
    loadSkills();
  }
}
