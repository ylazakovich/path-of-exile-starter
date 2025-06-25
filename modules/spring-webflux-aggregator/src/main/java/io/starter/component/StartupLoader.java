package io.starter.component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import io.starter.entity.UniqueJewelEntity;
import io.starter.model.ninja.UniqueJewel;
import io.starter.recipes.AnimaStoneRecipe;
import io.starter.service.DataAccessService;
import io.starter.service.NinjaDataSyncService;
import io.starter.service.PathOfExileService;
import io.starter.service.PoeNinjaService;
import io.starter.service.SkillDeltaService;
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
  private final VendorRecipeService vendorRecipeService;

  public void loadEverything() throws InterruptedException {
    stageLeagues();
    Thread.sleep(Duration.ofSeconds(2));
    stageCurrencies();
    stageUniqueJewels();
    stageSkills();
    Thread.sleep(Duration.ofSeconds(10));
    stageProcessedSkills();
    stageAnimaStoneRecipes();
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

  private void stageAnimaStoneRecipes() {
    dataAccessService.findLeagues().forEach(league -> {
      dataAccessService.findUniqueJewelByLeague(UniqueJewel.ResolvedName.ANIMA_STONE.value, league)
          .ifPresent(jewel -> {
            List<UniqueJewelEntity> ingredients = Stream.of(
                    dataAccessService.findUniqueJewelByLeague(UniqueJewel.ResolvedName.PRIMORDIAL_EMINENCE.value, league),
                    dataAccessService.findUniqueJewelByLeague(UniqueJewel.ResolvedName.PRIMORDIAL_HARMONY.value, league),
                    dataAccessService.findUniqueJewelByLeague(UniqueJewel.ResolvedName.PRIMORDIAL_MIGHT.value, league)
                ).flatMap(Optional::stream)
                .toList();
            AnimaStoneRecipe animaStoneRecipe = new AnimaStoneRecipe(jewel.getName(), league, jewel.getChaosEquivalent());
            vendorRecipeService.saveAnimaStoneRecipe(animaStoneRecipe, ingredients);
            log.info("Anima Stone recipe saved successfully for league '{}'", league.getName());
          });
    });
  }
}
