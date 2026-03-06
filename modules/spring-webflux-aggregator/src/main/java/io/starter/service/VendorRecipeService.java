package io.starter.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.starter.entity.LeagueEntity;
import io.starter.entity.VendorRecipeEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorRecipeService {

  private static final double PROFIT_THRESHOLD = 0.0;
  private final DataAccessService dataAccessService;
  private final VendorRecipeDataSyncService vendorRecipeDataSyncService;

  public void syncVendorRecipes(List<VendorRecipeEntity> recipes, LeagueEntity league) {
    Set<String> incomingRecipeNames = recipes.stream()
        .map(VendorRecipeEntity::getName)
        .collect(Collectors.toSet());
    dataAccessService.findVendorRecipesByLeague(league).stream()
        .filter(existingRecipe -> !incomingRecipeNames.contains(existingRecipe.getName()))
        .forEach(existingRecipe -> vendorRecipeDataSyncService.deleteByNameAndLeague(existingRecipe.getName(), league));
    recipes.forEach(recipe -> syncVendorRecipe(recipe, league));
  }

  private void syncVendorRecipe(VendorRecipeEntity recipe, LeagueEntity league) {
    if (recipe.getChaosEquivalentProfit() == null || recipe.getChaosEquivalentProfit() <= PROFIT_THRESHOLD) {
      vendorRecipeDataSyncService.deleteByNameAndLeague(recipe.getName(), league);
      return;
    }
    vendorRecipeDataSyncService.upsert(recipe, league);
  }
}
