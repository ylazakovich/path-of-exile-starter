package io.starter.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import io.starter.config.DivinationRecipeConfiguration;
import io.starter.entity.LeagueEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DivinationRecipeService {

  private final DataAccessService dataAccessService;
  private final DivinationRecipeConfiguration configuration;

  public List<DivinationRecipeProjection> findProfitableRecipes(LeagueEntity league) {
    if (league == null) {
      return List.of();
    }
    return configuration.getDefinitions().stream()
        .map(definition -> resolveRecipe(definition, league))
        .flatMap(Optional::stream)
        .filter(recipe -> recipe.profitChaos() > 0)
        .sorted(Comparator.comparingDouble(DivinationRecipeProjection::profitChaos).reversed())
        .toList();
  }

  private Optional<DivinationRecipeProjection> resolveRecipe(DivinationRecipeConfiguration.RecipeDefinition definition,
                                                             LeagueEntity league) {
    if (definition == null || isBlank(definition.getCardName()) || isBlank(definition.getResultName())) {
      return Optional.empty();
    }
    Optional<Double> maybeCardPrice = dataAccessService.findRateChaosEquivalentByName(definition.getCardName(), league);
    Optional<Double> maybeResultPrice = dataAccessService.findRateChaosEquivalentByName(
        definition.getResultName(),
        league
    );
    if (maybeCardPrice.isEmpty() || maybeResultPrice.isEmpty()) {
      log.debug(
          "Skipping divination recipe '{}' in league '{}': missing market prices for card='{}' or result='{}'",
          definition.getId(),
          league.getName(),
          definition.getCardName(),
          definition.getResultName()
      );
      return Optional.empty();
    }

    int stackSize = resolveQuantity(definition.getStackSize());
    int resultQuantity = resolveQuantity(definition.getResultQuantity());
    double cardPrice = maybeCardPrice.orElseThrow();
    double resultPrice = maybeResultPrice.orElseThrow();
    double costChaos = cardPrice * stackSize;
    double resultChaos = resultPrice * resultQuantity;
    double profitChaos = resultChaos - costChaos;
    double marginPercent = costChaos > 0 ? (profitChaos / costChaos) * 100 : 0.0;

    return Optional.of(new DivinationRecipeProjection(
        definition.getId(),
        definition.getCardName(),
        stackSize,
        definition.getResultName(),
        resultQuantity,
        cardPrice,
        resultPrice,
        costChaos,
        resultChaos,
        profitChaos,
        marginPercent
    ));
  }

  private int resolveQuantity(Integer quantity) {
    return quantity == null || quantity < 1 ? 1 : quantity;
  }

  private boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  public record DivinationRecipeProjection(
      String id,
      String cardName,
      int stackSize,
      String resultName,
      int resultQuantity,
      double cardPriceChaos,
      double resultUnitPriceChaos,
      double costChaos,
      double resultChaos,
      double profitChaos,
      double marginPercent
  ) {
  }
}
