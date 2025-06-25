package io.starter.service;

import java.util.List;

import io.starter.entity.LeagueEntity;
import io.starter.entity.VendorRecipeEntity;
import io.starter.model.ninja.UniqueJewel;
import io.starter.recipes.AnimaStoneRecipe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorRecipeService {

  private final DataAccessService dataAccessService;

  public void saveAnimaStoneRecipe(LeagueEntity league, AnimaStoneRecipe recipe, List<UniqueJewel> ingredients) {
    if (!recipe.matches(ingredients)) {
      throw new IllegalArgumentException("Invalid ingredients for Anima Stone recipe");
    }
    Double chaosEquivalentPrice = dataAccessService
        .findUniqueJewelByLeague(UniqueJewel.ResolvedName.ANIMA_STONE.value, league)
        .getChaosEquivalent();
    double profit = recipe.profit(ingredients);
    VendorRecipeEntity entity = new VendorRecipeEntity();
    entity.setLeague(league);
    entity.setName(UniqueJewel.ResolvedName.ANIMA_STONE.value);
    entity.setChaosEquivalentPrice(chaosEquivalentPrice);
    entity.setChaosEquivalentProfit(profit);
    dataAccessService.saveVendorRecipe(entity);
  }

  public boolean isAnimaRecipeSaved(LeagueEntity league) {
    return dataAccessService
        .findVendorRecipeByNameAndLeague(UniqueJewel.ResolvedName.ANIMA_STONE.value, league)
        .isPresent();
  }
}
