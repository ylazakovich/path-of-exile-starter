package io.starter.component;

import java.time.Duration;
import java.util.List;

import io.starter.entity.VendorRecipeEntity;
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

  private static final List<String> UNIQUE_ITEM_TYPES = List.of(
      "UniqueJewel",
      "UniqueArmour",
      "UniqueAccessory",
      "UniqueWeapon",
      "UniqueFlask",
      "UniqueMap"
  );
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
        UNIQUE_ITEM_TYPES.forEach(type -> poeNinjaService.getUniqueItems(league.getName(), type).subscribe(response -> {
          ninjaDataSyncService.addNewJewels(response.getBody(), league);
          log.info("{} - {} - Loaded {} units",
              league.getName(),
              type,
              dataAccessService.findUniqueJewelsByLeague(league).size());
        })));

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
      List<VendorRecipeEntity> calculatedRecipes = vendorRecipeCalculatorService.calculateRecipesForLeague(league);
      vendorRecipeService.syncVendorRecipes(calculatedRecipes, league);
      int persistedRecipes = dataAccessService.findVendorRecipesByLeague(league).size();
      if (persistedRecipes == 0) {
        log.warn(
            "Vendor recipes sync completed for league '{}': no recipes available (calculated={}, persisted={})",
            league.getName(),
            calculatedRecipes.size(),
            persistedRecipes
        );
        return;
      }
      log.info(
          "Vendor recipes sync completed for league '{}': calculated={}, persisted={}",
          league.getName(),
          calculatedRecipes.size(),
          persistedRecipes
      );
    });
  }
}
