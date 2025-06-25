package io.starter.service;

import java.util.List;

import io.starter.entity.UniqueJewelEntity;
import io.starter.entity.VendorRecipeEntity;
import io.starter.recipes.AnimaStoneRecipe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorRecipeService {

  private final DataAccessService dataAccessService;
  private final VendorRecipeDataSyncService vendorRecipeDataSyncService;

  public void saveAnimaStoneRecipe(AnimaStoneRecipe recipe,
                                   List<UniqueJewelEntity> ingredients) {
    VendorRecipeEntity entity = recipe.craft(ingredients);
    vendorRecipeDataSyncService.load(entity, recipe.getLeagueEntity());
  }

  public void updateAnimaStoneRecipe(AnimaStoneRecipe recipe,
                                     List<UniqueJewelEntity> ingredients) {
    dataAccessService.findVendorRecipeByNameAndLeague(recipe.getName(), recipe.getLeagueEntity())
        .ifPresent(vendorRecipe -> {
          VendorRecipeEntity updatedValues = recipe.craft(ingredients);
          vendorRecipe.setChaosEquivalentPrice(updatedValues.getChaosEquivalentPrice());
          vendorRecipe.setChaosEquivalentProfit(updatedValues.getChaosEquivalentProfit());
          vendorRecipeDataSyncService.save(vendorRecipe);
        });
  }
}
