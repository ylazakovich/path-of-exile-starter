package io.starter.controller;

import java.util.List;

import io.starter.config.ScheduleConfig;
import io.starter.entity.LeagueEntity;
import io.starter.entity.VendorRecipeEntity;
import io.starter.service.DataAccessService;
import io.starter.service.VendorRecipeCalculatorService;
import io.starter.service.VendorRecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendor-recipes")
@RequiredArgsConstructor
@Log4j2
public class VendorRecipeController {

  private final DataAccessService dataAccessService;
  private final VendorRecipeCalculatorService vendorRecipeCalculatorService;
  private final VendorRecipeService vendorRecipeService;

  @GetMapping
  public List<VendorRecipeEntity> getVendorRecipesByLeague(@RequestParam("league") String league) {
    LeagueEntity leagueEntity = dataAccessService.findLeagueByName(league);
    if (leagueEntity == null) {
      return List.of();
    }
    return dataAccessService.findVendorRecipesByLeague(leagueEntity);
  }

  @GetMapping("/diagnostics")
  public List<VendorRecipeCalculatorService.RecipeDiagnostic> getVendorRecipeDiagnostics(@RequestParam("league") String league) {
    LeagueEntity leagueEntity = dataAccessService.findLeagueByName(league);
    if (leagueEntity == null) {
      return List.of();
    }
    return vendorRecipeCalculatorService.diagnoseRecipesForLeague(leagueEntity);
  }

  @PostMapping("/load")
  public void loadVendorRecipes() {
    dataAccessService.findLeagues().forEach(this::loadVendorRecipesForLeague);
  }

  @Scheduled(cron = ScheduleConfig.A8R_VENDOR_RECIPES_UPDATE_CRON)
  public void updateVendorRecipes() {
    dataAccessService.findLeagues().forEach(this::loadVendorRecipesForLeague);
  }

  private void loadVendorRecipesForLeague(LeagueEntity league) {
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
  }
}
