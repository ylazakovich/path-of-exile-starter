package io.starter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import io.starter.config.VendorRecipeConfiguration;
import io.starter.entity.LeagueEntity;
import io.starter.entity.UniqueJewelEntity;
import io.starter.entity.VendorRecipeEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class VendorRecipeCalculatorService {

  private final DataAccessService dataAccessService;
  private final VendorRecipeConfiguration vendorRecipeConfiguration;

  public List<VendorRecipeEntity> calculateRecipesForLeague(LeagueEntity league) {
    return vendorRecipeConfiguration.getDefinitions()
        .stream()
        .map(definition -> calculateRecipe(league, definition))
        .flatMap(Optional::stream)
        .toList();
  }

  private Optional<VendorRecipeEntity> calculateRecipe(LeagueEntity league,
                                                       VendorRecipeConfiguration.RecipeDefinition definition) {
    if (isBlank(definition.getResultItemName()) || isBlank(definition.getName())) {
      log.warn("Recipe '{}' is skipped: 'name' or 'resultItemName' is empty", definition.getId());
      return Optional.empty();
    }
    Optional<UniqueJewelEntity> result = dataAccessService.findUniqueJewelByLeague(definition.getResultItemName(), league);
    if (result.isEmpty()) {
      log.warn("Recipe '{}' is skipped for league '{}': result '{}' was not found",
          definition.getId(), league.getName(), definition.getResultItemName());
      return Optional.empty();
    }
    List<UniqueJewelEntity> ingredients = new ArrayList<>();
    List<String> missingIngredients = new ArrayList<>();
    List<String> recipeIngredients = definition.getIngredients() == null ? List.of() : definition.getIngredients();
    for (String ingredientName : recipeIngredients) {
      dataAccessService.findUniqueJewelByLeague(ingredientName, league)
          .ifPresentOrElse(ingredients::add, () -> missingIngredients.add(ingredientName));
    }
    if (!missingIngredients.isEmpty()) {
      log.warn("Recipe '{}' is skipped for league '{}': missing ingredients {}",
          definition.getId(), league.getName(), missingIngredients);
      return Optional.empty();
    }
    double cost = ingredients.stream()
        .map(UniqueJewelEntity::getChaosEquivalent)
        .filter(Objects::nonNull)
        .mapToDouble(Double::doubleValue)
        .sum();
    double resultPrice = result.get().getChaosEquivalent();
    VendorRecipeEntity entity = new VendorRecipeEntity(definition.getName(), resultPrice, resultPrice - cost);
    entity.setLeague(league);
    return Optional.of(entity);
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }
}
