package io.starter.controller;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

import io.starter.config.ScheduleConfig;
import io.starter.entity.LeagueEntity;
import io.starter.service.AnalyzerService;
import io.starter.service.DataAccessService;
import io.starter.service.NinjaDataSyncService;
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

  private final DataAccessService dataAccessService;
  private final NinjaDataSyncService ninjaDataSyncService;
  private final PoeNinjaService poeNinjaService;
  private final PathOfExileService pathOfExileService;
  private final AnalyzerService analyzerService;

  public DatabaseController(DataAccessService dataAccessService,
                            NinjaDataSyncService ninjaDataSyncService,
                            PoeNinjaService poeNinjaService,
                            PathOfExileService pathOfExileService,
                            AnalyzerService analyzerService) {
    this.dataAccessService = dataAccessService;
    this.ninjaDataSyncService = ninjaDataSyncService;
    this.poeNinjaService = poeNinjaService;
    this.pathOfExileService = pathOfExileService;
    this.analyzerService = analyzerService;
  }

  @PostMapping("/load/jewels")
  public void loadUniqueJewels() {
    dataAccessService.findLeagues().forEach(this::loadUniqueJewels);
  }

  private void loadUniqueJewels(LeagueEntity league) {
    poeNinjaService.getUniqueJewels(league.getName())
        .subscribe(response -> {
          ninjaDataSyncService.loadUniqueJewels(response.getBody(), league);
          log.info("{} - Loaded {} Unique Jewels",
              league.getName(),
              dataAccessService.findUniqueJewelsByLeague(league).size()
          );
        });
  }

  @PostMapping("/load/rates")
  public void loadRates() {
    dataAccessService.findLeagues().forEach(this::loadRates);
  }

  private void loadRates(LeagueEntity league) {
    poeNinjaService.getRates(league.getName())
        .subscribe(response -> {
          ninjaDataSyncService.loadCurrency(response.getBody(), league);
          log.info("{} - Loaded {} Rates",
              league.getName(),
              dataAccessService.findRatesByLeague(league).size()
          );
        });
  }

  @PostMapping("/load/skills")
  public void loadSkills() {
    dataAccessService.findLeagues().forEach(this::loadSkills);
  }

  private void loadSkills(LeagueEntity league) {
    poeNinjaService.getSkills(league.getName())
        .subscribe(response -> {
          ninjaDataSyncService.loadSkills(response.getBody(), league);
          log.info("{} - Loaded {} Skills",
              league.getName(),
              dataAccessService.findSkillsByLeague(league).size()
          );
        });
  }

  @GetMapping("/leagues")
  public ResponseEntity<List<LeagueEntity>> getLeagues() {
    return ResponseEntity.ok(dataAccessService.findLeagues());
  }

  @PostMapping("/load/leagues")
  public void loadLeagues() {
    pathOfExileService.getAllLeagues().subscribe(response -> {
      dataAccessService.saveLeagues(response.getBody());
      log.info("Loaded {} leagues", dataAccessService.findLeagues().size());
    });
  }

  @Scheduled(cron = ScheduleConfig.A8R_ADD_CRON)
  public void updateRates() {
    dataAccessService.findLeagues()
        .forEach(league -> poeNinjaService.getRates(league.getName())
            .subscribe(response -> ninjaDataSyncService.updateCurrencies(response.getBody(), league)));
  }

  @Scheduled(cron = ScheduleConfig.A8R_UPDATE_CRON)
  public void updateSkills() {
    dataAccessService.findLeagues()
        .forEach(league -> poeNinjaService.getSkills(league.getName())
            .subscribe(response -> ninjaDataSyncService.updateSkills(response.getBody(), league)));
  }

  @Scheduled(cron = ScheduleConfig.A8R_ADD_CRON)
  public void addNewSkills() {
    dataAccessService.findLeagues()
        .forEach(league -> poeNinjaService.getSkills(league.getName())
            .subscribe(response -> ninjaDataSyncService.addNew(Objects.requireNonNull(response.getBody()), league)));
  }

  public void loadProcessedSkills() {
    dataAccessService.findLeagues().forEach(
        league -> {
          dataAccessService.addProcessedSkills(league, analyzerService.analyzeSkills(league.getName()));
          log.info("{} - Processed {} Skills",
              league.getName(),
              dataAccessService.findProcessedSkillsByLeague(league).size()
          );
        }
    );
  }

  @Scheduled(cron = ScheduleConfig.A8R_ADD_CRON)
  private void addNewProcessedSkills() {
    dataAccessService.findLeagues()
        .forEach(league ->
            dataAccessService.addNewProcessedSkill(league, analyzerService.analyzeSkills(league.getName())));
  }

  @Scheduled(cron = ScheduleConfig.A8R_UPDATE_CRON)
  private void updateProcessedSkills() {
    dataAccessService.findLeagues()
        .forEach(league ->
            dataAccessService.updateProcessedSkills(league, analyzerService.analyzeSkills(league.getName())));
  }

  @SneakyThrows(InterruptedException.class)
  public void loading() {
    loadLeagues();
    Thread.sleep(Duration.ofSeconds(2));
    loadRates();
    loadUniqueJewels();
    loadSkills();
    Thread.sleep(Duration.ofSeconds(10));
    loadProcessedSkills();
  }
}
