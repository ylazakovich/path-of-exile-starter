package io.starter.service;

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

    double profit = recipe.profit(ingredients);

    VendorRecipeEntity entity = new VendorRecipeEntity();
    entity.setLeague(league);
    entity.setName(UniqueJewel.ResolvedName.ANIMA_STONE.value);
    entity.setChaosEquivalentPrice(recipe.getResultChaosEquivalent());
    entity.setChaosEquivalentProfit(profit);

    vendorRecipeRepository.save(entity);
  }

  public boolean isAnimaRecipeSaved(LeagueEntity league) {
    return vendorRecipeRepository.findByLeagueAndName(league, UniqueJewel.ResolvedName.ANIMA_STONE.value).isPresent();
  }
}
