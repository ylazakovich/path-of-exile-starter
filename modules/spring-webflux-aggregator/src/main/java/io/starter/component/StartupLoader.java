package io.starter.component;

import java.time.Duration;

import io.starter.service.AnalyzerService;
import io.starter.service.DataAccessService;
import io.starter.service.NinjaDataSyncService;
import io.starter.service.PathOfExileService;
import io.starter.service.PoeNinjaService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class StartupLoader {

  private final DataAccessService dataAccessService;
  private final NinjaDataSyncService ninjaDataSyncService;
  private final PoeNinjaService poeNinjaService;
  private final PathOfExileService pathOfExileService;
  private final AnalyzerService analyzerService;

  public void loadEverything() throws InterruptedException {
    log.info("🚀 Starting full init...");
    pathOfExileService.getAllLeagues().subscribe(response -> {
      dataAccessService.saveLeagues(response.getBody());
      log.info("Loaded {} leagues", dataAccessService.findLeagues().size());
    });
    Thread.sleep(Duration.ofSeconds(2));
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getRates(league.getName()).subscribe(response -> {
          ninjaDataSyncService.loadCurrency(response.getBody(), league);
          log.info("{} - Loaded {} Rates", league.getName(), dataAccessService.findRatesByLeague(league).size());
        }));
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getUniqueJewels(league.getName()).subscribe(response -> {
          ninjaDataSyncService.loadUniqueJewels(response.getBody(), league);
          log.info("{} - Loaded {} Jewels", league.getName(), dataAccessService.findUniqueJewelsByLeague(league).size());
        }));
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getSkills(league.getName()).subscribe(response -> {
          ninjaDataSyncService.loadSkills(response.getBody(), league);
          log.info("{} - Loaded {} Skills", league.getName(), dataAccessService.findSkillsByLeague(league).size());
        }));
    Thread.sleep(Duration.ofSeconds(10));
    dataAccessService.findLeagues().forEach(league -> {
      dataAccessService.addProcessedSkills(league, analyzerService.analyzeSkills(league.getName()));
      log.info("{} - Processed {} Skills", league.getName(), dataAccessService.findProcessedSkillsByLeague(league).size());
    });
    log.info("✅ Full init completed.");
  }
}
