package io.starter.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import io.starter.config.VendorRecipeConfiguration;
import io.starter.entity.LeagueEntity;
import io.starter.entity.RateEntity;
import io.starter.entity.SkillEntity;
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

  public List<RecipeDiagnostic> diagnoseRecipesForLeague(LeagueEntity league) {
    return vendorRecipeConfiguration.getDefinitions().stream()
        .map(definition -> diagnoseRecipe(league, definition))
        .toList();
  }

  private Optional<VendorRecipeEntity> calculateRecipe(LeagueEntity league,
                                                       VendorRecipeConfiguration.RecipeDefinition definition) {
    String recipeId = safeRecipeId(definition);
    String resultName = resolveResultName(definition);
    VendorRecipeConfiguration.PriceSource resultSource = resolveResultSource(definition);
    if (isBlank(resultName) || isBlank(definition.getName())) {
      log.warn("Recipe '{}' is skipped: 'name' is empty", recipeId);
      return Optional.empty();
    }
    PriceResolution resultPrice = resolvePrice(resultSource, resultName, league);
    logIfAmbiguous(recipeId, resultName, resultSource, resultPrice, league);
    if (resultPrice.selectedPrice().isEmpty()) {
      log.warn("Recipe '{}' is skipped for league '{}': result '{}' was not found",
          recipeId, league.getName(), resultName);
      return Optional.empty();
    }
    double cost = 0.0;
    List<String> missingIngredients = new ArrayList<>();
    List<VendorRecipeConfiguration.IngredientDefinition> recipeIngredients =
        definition.getIngredients() == null ? List.of() : definition.getIngredients();
    for (VendorRecipeConfiguration.IngredientDefinition ingredient : recipeIngredients) {
      if (ingredient == null || isBlank(ingredient.getName())) {
        continue;
      }
      PriceResolution ingredientPrice = resolvePrice(ingredient.getPriceSource(), ingredient.getName(), league);
      logIfAmbiguous(recipeId, ingredient.getName(), ingredient.getPriceSource(), ingredientPrice, league);
      if (ingredientPrice.selectedPrice().isPresent()) {
        cost += ingredientPrice.selectedPrice().get() * resolveQuantity(ingredient.getQuantity());
      } else {
        missingIngredients.add(ingredient.getName());
      }
    }
    if (!missingIngredients.isEmpty()) {
      log.warn("Recipe '{}' is skipped for league '{}': missing ingredients {}",
          recipeId, league.getName(), missingIngredients);
      return Optional.empty();
    }
    double resultChaos = resultPrice.selectedPrice().orElseThrow();
    VendorRecipeEntity entity = new VendorRecipeEntity(definition.getName(), resultChaos, resultChaos - cost);
    entity.setLeague(league);
    return Optional.of(entity);
  }

  private RecipeDiagnostic diagnoseRecipe(LeagueEntity league, VendorRecipeConfiguration.RecipeDefinition definition) {
    String recipeId = safeRecipeId(definition);
    String resultName = resolveResultName(definition);
    VendorRecipeConfiguration.PriceSource resultSource = resolveResultSource(definition);
    ItemDiagnostic resultDiagnostic = toDiagnostic(
        resultName,
        1,
        resultSource,
        resolvePrice(resultSource, resultName, league)
    );
    List<VendorRecipeConfiguration.IngredientDefinition> ingredients =
        definition.getIngredients() == null
            ? List.of()
            : definition.getIngredients();
    List<ItemDiagnostic> ingredientDiagnostics = ingredients
        .stream()
        .filter(ingredient -> ingredient != null && !isBlank(ingredient.getName()))
        .map(ingredient -> toDiagnostic(
            ingredient.getName(),
            resolveQuantity(ingredient.getQuantity()),
            ingredient.getPriceSource(),
            resolvePrice(ingredient.getPriceSource(), ingredient.getName(), league)
        ))
        .toList();
    boolean hasMissing = Stream.concat(Stream.of(resultDiagnostic), ingredientDiagnostics.stream())
        .anyMatch(diagnostic -> "missing".equals(diagnostic.status()));
    boolean hasAmbiguous = Stream.concat(Stream.of(resultDiagnostic), ingredientDiagnostics.stream())
        .anyMatch(diagnostic -> "ambiguous".equals(diagnostic.status()));
    return new RecipeDiagnostic(recipeId, definition.getName(), resultDiagnostic, ingredientDiagnostics, hasMissing, hasAmbiguous);
  }

  private String resolveResultName(VendorRecipeConfiguration.RecipeDefinition definition) {
    if (definition.getResult() != null && !isBlank(definition.getResult().getName())) {
      return definition.getResult().getName();
    }
    return definition.getName();
  }

  private VendorRecipeConfiguration.PriceSource resolveResultSource(VendorRecipeConfiguration.RecipeDefinition definition) {
    if (definition.getResult() != null && definition.getResult().getPriceSource() != null) {
      return definition.getResult().getPriceSource();
    }
    return VendorRecipeConfiguration.PriceSource.AUTO;
  }

  private PriceResolution resolvePrice(VendorRecipeConfiguration.PriceSource source,
                                       String name,
                                       LeagueEntity league) {
    List<Double> candidates = switch (source) {
      case UNIQUE_ITEM -> toUniqueItemPrices(dataAccessService.findUniqueJewelsByNameAndLeague(name, league));
      case CURRENCY -> toCurrencyPrices(dataAccessService.findRatesByNameAndLeague(name, league));
      case SKILL_GEM -> toSkillGemPrices(dataAccessService.findSkillsByLeague(league), name, true);
      case AUTO -> {
        List<Double> prices = new ArrayList<>();
        prices.addAll(toUniqueItemPrices(dataAccessService.findUniqueJewelsByNameAndLeague(name, league)));
        prices.addAll(toCurrencyPrices(dataAccessService.findRatesByNameAndLeague(name, league)));
        prices.addAll(toSkillGemPrices(dataAccessService.findSkillsByLeague(league), name, false));
        yield prices;
      }
    };
    Optional<Double> selected = candidates.stream().min(Comparator.naturalOrder());
    return new PriceResolution(candidates.size(), selected);
  }

  private List<Double> toUniqueItemPrices(List<UniqueJewelEntity> items) {
    boolean hasLinkedVariants = items.stream()
        .anyMatch(item -> item.getLinks() != null);
    if (hasLinkedVariants) {
      return items.stream()
          .filter(item -> Integer.valueOf(4).equals(item.getLinks()))
          .map(UniqueJewelEntity::getChaosEquivalent)
          .filter(Objects::nonNull)
          .distinct()
          .toList();
    }
    return items.stream()
        .filter(item -> item.getLinks() == null)
        .map(UniqueJewelEntity::getChaosEquivalent)
        .filter(Objects::nonNull)
        .distinct()
        .toList();
  }

  private List<Double> toCurrencyPrices(List<RateEntity> rates) {
    return rates.stream()
        .map(RateEntity::getChaosEquivalent)
        .filter(Objects::nonNull)
        .toList();
  }

  private List<Double> toSkillGemPrices(List<SkillEntity> skills, String name, boolean onlyQualityAtLeastOne) {
    return skills.stream()
        .filter(skill -> name.equals(skill.getName()))
        .filter(skill -> !onlyQualityAtLeastOne || skill.getGemQuality() >= 1)
        .map(SkillEntity::getChaosEquivalent)
        .filter(Objects::nonNull)
        .toList();
  }

  private ItemDiagnostic toDiagnostic(String name,
                                      int quantity,
                                      VendorRecipeConfiguration.PriceSource source,
                                      PriceResolution resolution) {
    VendorRecipeConfiguration.PriceSource effectiveSource =
        source == null ? VendorRecipeConfiguration.PriceSource.AUTO : source;
    String status = resolution.selectedPrice().isEmpty()
        ? "missing"
        : resolution.matches() > 1 ? "ambiguous" : "ok";
    return new ItemDiagnostic(
        name,
        quantity,
        effectiveSource.name().toLowerCase(),
        resolution.matches(),
        resolution.selectedPrice().orElse(null),
        status
    );
  }

  private void logIfAmbiguous(String recipeId,
                              String itemName,
                              VendorRecipeConfiguration.PriceSource source,
                              PriceResolution resolution,
                              LeagueEntity league) {
    if (resolution.matches() > 1) {
      VendorRecipeConfiguration.PriceSource effectiveSource =
          source == null ? VendorRecipeConfiguration.PriceSource.AUTO : source;
      log.warn(
          "Recipe '{}' in league '{}' has {} price candidates for '{}' (source={}); using minimum price",
          recipeId,
          league.getName(),
          resolution.matches(),
          itemName,
          effectiveSource
      );
    }
  }

  private String safeRecipeId(VendorRecipeConfiguration.RecipeDefinition definition) {
    return isBlank(definition.getId()) ? definition.getName() : definition.getId();
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  private int resolveQuantity(Integer quantity) {
    return quantity == null || quantity < 1 ? 1 : quantity;
  }

  public record ItemDiagnostic(
      String name,
      Integer quantity,
      String source,
      Integer matches,
      Double selectedChaosEquivalent,
      String status
  ) {
  }

  public record RecipeDiagnostic(
      String recipeId,
      String recipeName,
      ItemDiagnostic result,
      List<ItemDiagnostic> ingredients,
      boolean hasMissing,
      boolean hasAmbiguous
  ) {
  }

  private record PriceResolution(
      int matches,
      Optional<Double> selectedPrice
  ) {
  }
}
