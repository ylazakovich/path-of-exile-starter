package io.starter.recipes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.starter.entity.LeagueEntity;
import io.starter.entity.UniqueJewelEntity;
import io.starter.entity.VendorRecipeEntity;
import io.starter.model.ninja.UniqueJewel;

public class AnimaStoneRecipe implements Recipe<UniqueJewelEntity, VendorRecipeEntity> {

  private static final Set<String> REQUIRED_JEWEL_NAMES = Set.of(
      UniqueJewel.ResolvedName.PRIMORDIAL_EMINENCE.value,
      UniqueJewel.ResolvedName.PRIMORDIAL_HARMONY.value,
      UniqueJewel.ResolvedName.PRIMORDIAL_MIGHT.value
  );

  private final String name;
  private final LeagueEntity leagueEntity;
  private final double resultChaosEquivalent;

  public AnimaStoneRecipe(String name,
                          LeagueEntity leagueEntity,
                          double resultChaosEquivalent) {
    this.name = name;
    this.leagueEntity = leagueEntity;
    this.resultChaosEquivalent = resultChaosEquivalent;
  }

  @Override
  public boolean matches(List<UniqueJewelEntity> ingredients) {
    Set<String> inputNames = ingredients.stream()
        .map(UniqueJewelEntity::getName)
        .collect(Collectors.toSet());
    return inputNames.containsAll(REQUIRED_JEWEL_NAMES);
  }

  @Override
  public VendorRecipeEntity craft(List<UniqueJewelEntity> ingredients) {
    if (!matches(ingredients)) {
      throw new IllegalArgumentException("Invalid jewels for Anima Stone recipe");
    }
    VendorRecipeEntity vendorRecipeEntity = new VendorRecipeEntity(name, resultChaosEquivalent, profit(ingredients));
    vendorRecipeEntity.setLeague(leagueEntity);
    return vendorRecipeEntity;
  }

  @Override
  public double profit(List<UniqueJewelEntity> ingredients) {
    double cost = ingredients.stream()
        .mapToDouble(UniqueJewelEntity::getChaosEquivalent)
        .sum();
    return resultChaosEquivalent - cost;
  }

  @Override
  public LeagueEntity getLeagueEntity() {
    return leagueEntity;
  }

  @Override
  public String getName() {
    return name;
  }
}
