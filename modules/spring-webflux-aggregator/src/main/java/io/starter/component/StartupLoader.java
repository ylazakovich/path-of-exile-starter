package io.starter.component;

import java.time.Duration;

import io.starter.service.DataAccessService;
import io.starter.service.NinjaDataSyncService;
import io.starter.service.PathOfExileService;
import io.starter.service.PoeNinjaService;
import io.starter.service.SkillDeltaService;
import io.starter.service.VendorRecipeCalculatorService;
import io.starter.service.VendorRecipeService;

import lombok.RequiredArgsConstructor;
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
  private final SkillDeltaService skillDeltaService;
  private final VendorRecipeCalculatorService vendorRecipeCalculatorService;
  private final VendorRecipeService vendorRecipeService;

  public void loadEverything() throws InterruptedException {
    stageLeagues();
    Thread.sleep(Duration.ofSeconds(2));
    stageCurrencies();
    stageUniqueJewels();
    stageSkills();
    Thread.sleep(Duration.ofSeconds(10));
    stageProcessedSkills();
    stageVendorRecipes();
  }

  private void stageLeagues() {
    pathOfExileService.getAllLeagues().subscribe(response -> {
      dataAccessService.saveLeagues(response.getBody());
      log.info("Loaded {} leagues", dataAccessService.findLeagues().size());
    });
  }

  private void stageCurrencies() {
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getRates(league.getName()).subscribe(response -> {
          ninjaDataSyncService.loadCurrency(response.getBody(), league);
          log.info("{} - Currency - Loaded {} units",
              league.getName(),
              dataAccessService.findRatesByLeague(league).size());
        }));
  }

  private void stageUniqueJewels() {
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getUniqueJewels(league.getName()).subscribe(response -> {
          ninjaDataSyncService.loadUniqueJewels(response.getBody(), league);
          log.info("{} - Unique Jewel - Loaded {} units",
              league.getName(),
              dataAccessService.findUniqueJewelsByLeague(league).size());
        }));

  }

  private void stageSkills() {
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getSkills(league.getName()).subscribe(response -> {
          ninjaDataSyncService.loadSkills(response.getBody(), league);
          log.info("{} - Skill - Loaded {} units",
              league.getName(),
              dataAccessService.findSkillsByLeague(league).size());
        }));
  }

  private void stageProcessedSkills() {
    dataAccessService.findLeagues().forEach(league -> {
      dataAccessService.addProcessedSkills(league, skillDeltaService.analyzeSkills(league.getName()));
      log.info("{} - Processed Skill - Processed {} units",
          league.getName(),
          dataAccessService.findProcessedSkillsByLeague(league).size());
    });
  }

  private void stageVendorRecipes() {
    dataAccessService.findLeagues().forEach(league -> {
      vendorRecipeService.syncVendorRecipes(vendorRecipeCalculatorService.calculateRecipesForLeague(league), league);
      log.info("Vendor recipes loaded successfully for league '{}'", league.getName());
    });
  }
}
