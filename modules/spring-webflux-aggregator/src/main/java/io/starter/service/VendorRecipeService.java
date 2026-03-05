package io.starter.service;

import java.util.List;

import io.starter.entity.LeagueEntity;
import io.starter.entity.VendorRecipeEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorRecipeService {

  private static final double PROFIT_THRESHOLD = 0.0;
  private final VendorRecipeDataSyncService vendorRecipeDataSyncService;

  public void syncVendorRecipes(List<VendorRecipeEntity> recipes, LeagueEntity league) {
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
