package io.starter.recipes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.starter.model.ninja.UniqueJewel;

public class AnimaStoneRecipe implements Recipe<UniqueJewel, UniqueJewel> {

  private static final Set<String> REQUIRED_JEWEL_NAMES = Set.of(
      UniqueJewel.ResolvedName.PRIMORDIAL_EMINENCE.value,
      UniqueJewel.ResolvedName.PRIMORDIAL_HARMONY.value,
      UniqueJewel.ResolvedName.PRIMORDIAL_MIGHT.value
  );

  private final double resultChaosEquivalent;

  public AnimaStoneRecipe(double resultChaosEquivalent) {
    this.resultChaosEquivalent = resultChaosEquivalent;
  }

  @Override
  public boolean matches(List<UniqueJewel> ingredients) {
    Set<String> inputNames = ingredients.stream()
        .map(UniqueJewel::getName)
        .collect(Collectors.toSet());
    return inputNames.containsAll(REQUIRED_JEWEL_NAMES);
  }

  @Override
  public UniqueJewel craft(List<UniqueJewel> ingredients) {
    if (!matches(ingredients)) {
      throw new IllegalArgumentException("Invalid jewels for Anima Stone recipe");
    }
    return new UniqueJewel(UniqueJewel.ResolvedName.ANIMA_STONE.value, resultChaosEquivalent);
  }

  @Override
  public List<UniqueJewel> required() {
    return REQUIRED_JEWEL_NAMES.stream()
        .map(name -> new UniqueJewel(name, 0.0))
        .toList();
  }

  public double profit(List<UniqueJewel> ingredients) {
    double cost = ingredients.stream()
        .mapToDouble(UniqueJewel::getChaosEquivalent)
        .sum();
    return resultChaosEquivalent - cost;
  }
}
