package io.starter.service;

import java.util.List;

import io.starter.entity.LeagueEntity;
import io.starter.entity.UniqueJewelEntity;
import io.starter.entity.VendorRecipeEntity;
import io.starter.recipes.AnimaStoneRecipe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorRecipeService {

  private final VendorRecipeDataSyncService vendorRecipeDataSyncService;

  public boolean saveAnimaStoneRecipe(LeagueEntity league,
                                      AnimaStoneRecipe recipe,
                                      List<UniqueJewelEntity> ingredients) {
    VendorRecipeEntity entity = recipe.craft(ingredients);
    entity.setLeague(league);
    vendorRecipeDataSyncService.load(entity, league);
    return true;
  }
}
