package io.starter.controller;

import java.util.List;
import java.util.Objects;

import io.starter.entity.LeagueEntity;
import io.starter.service.DatabaseNinjaService;
import io.starter.service.DatabasePathOfExileService;
import io.starter.service.PathOfExileService;
import io.starter.service.PoeNinjaService;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping("/load/rates")
  public void loadRates() {
    databasePathOfExileService.readAll().forEach(this::loadRates);
  }

  private void loadRates(LeagueEntity league) {
    poeNinjaService.getRates(league.getName())
        .subscribe(data -> databaseNinjaService.loadCurrency(data.getBody(), league));
  }

  @PostMapping("/load/skills")
  public void loadSkills() {
    databasePathOfExileService.readAll().forEach(this::loadSkills);
  }

  private void loadSkills(LeagueEntity league) {
    poeNinjaService.getSkills(league.getName())
        .subscribe(data -> databaseNinjaService.loadSkills(data.getBody(), league));
  }

  @GetMapping ("/leagues")
  public ResponseEntity<List<LeagueEntity>> getLeagues() {
    return ResponseEntity.ok(databasePathOfExileService.readAll());
  }

  @PostMapping("/load/leagues")
  public void loadLeagues() {
    pathOfExileService.getAllLeagues().subscribe(data -> databasePathOfExileService.load(data.getBody()));
  }

  @Scheduled(cron = "0 */5 * * * *")
  public void updateRates() {
    databasePathOfExileService.readAll()
        .forEach(league -> poeNinjaService.getRates(league.getName())
            .subscribe(data -> databaseNinjaService.updateCurrencies(data.getBody(), league)));
  }

  @Scheduled(cron = "0 */5 * * * *")
  public void updateSkills() {
    databasePathOfExileService.readAll()
        .forEach(league -> poeNinjaService.getSkills(league.getName())
            .subscribe(data -> databaseNinjaService.updateSkills(data.getBody(), league)));
  }

  @Scheduled(cron = "0 */2 * * * *")
  public void addNewSkills() {
    databasePathOfExileService.readAll()
        .forEach(league -> poeNinjaService.getSkills(league.getName())
            .subscribe(data -> databaseNinjaService.addNew(Objects.requireNonNull(data.getBody()), league)));
  }

  @EventListener(ApplicationReadyEvent.class)
  public void loadDataAfterStartup() {
    loadLeagues();
    loadRates();
    loadSkills();
  }
}
