package io.starter.component;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
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
    pathOfExileService.getAllLeagues().subscribe(response -> {
      dataAccessService.saveLeagues(response.getBody());
      log.info("Loaded {} leagues", dataAccessService.findLeagues().size());
    });
    Thread.sleep(Duration.ofSeconds(2));
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getRates(league.getName()).subscribe(response -> {
          ninjaDataSyncService.loadCurrency(response.getBody(), league);
          log.info("{} - Currency - Loaded {} units",
              league.getName(),
              dataAccessService.findRatesByLeague(league).size());
        }));
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getUniqueJewels(league.getName()).subscribe(response -> {
          ninjaDataSyncService.loadUniqueJewels(response.getBody(), league);
          log.info("{} - Unique Jewel - Loaded {} units",
              league.getName(),
              dataAccessService.findUniqueJewelsByLeague(league).size());
        }));
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getSkills(league.getName()).subscribe(response -> {
          ninjaDataSyncService.loadSkills(response.getBody(), league);
          log.info("{} - Skill - Loaded {} units",
              league.getName(),
              dataAccessService.findSkillsByLeague(league).size());
        }));
    Thread.sleep(Duration.ofSeconds(10));
    dataAccessService.findLeagues().forEach(league -> {
      dataAccessService.addProcessedSkills(league, skillDeltaService.analyzeSkills(league.getName()));
      log.info("{} - Processed Skill - Processed {} units",
          league.getName(),
          dataAccessService.findProcessedSkillsByLeague(league).size());
      UniqueJewelEntity animaStone =
          dataAccessService.findUniqueJewelByLeague(UniqueJewel.ResolvedName.ANIMA_STONE.value, league);
      if (Objects.nonNull(animaStone)) {
        List<UniqueJewelEntity> ingredients = Stream.of(
            dataAccessService.findUniqueJewelByLeague(UniqueJewel.ResolvedName.PRIMORDIAL_EMINENCE.value, league),
            dataAccessService.findUniqueJewelByLeague(UniqueJewel.ResolvedName.PRIMORDIAL_HARMONY.value, league),
            dataAccessService.findUniqueJewelByLeague(UniqueJewel.ResolvedName.PRIMORDIAL_MIGHT.value, league)
        ).filter(Objects::nonNull).toList();
        AnimaStoneRecipe animaStoneRecipe =
            new AnimaStoneRecipe(Objects.requireNonNullElse(animaStone.getChaosEquivalent(), 0.0));
        boolean saved = vendorRecipeService.saveAnimaStoneRecipe(league, animaStoneRecipe, ingredients);
        if (saved) {
          log.info("Anima Stone recipe saved successfully for league '{}'", league.getName());
        }
      }
    });
  }
}
