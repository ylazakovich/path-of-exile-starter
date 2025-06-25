package io.starter.controller;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import io.starter.entity.LeagueEntity;
import io.starter.entity.UniqueJewelEntity;
import io.starter.model.ninja.UniqueJewel;
import io.starter.recipes.AnimaStoneRecipe;
import io.starter.service.DataAccessService;
import io.starter.service.VendorRecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendor-recipes")
@RequiredArgsConstructor
@Log4j2
public class VendorRecipeController {

  private final DataAccessService dataAccessService;
  private final VendorRecipeService vendorRecipeService;

  @PostMapping("/anima-stone/load")
  public void loadUniqueAnimaStones() {
    dataAccessService.findLeagues().forEach(this::loadUniqueAnimaStones);
  }

  private void loadUniqueAnimaStones(LeagueEntity league) {
    UniqueJewelEntity animaStone =
        dataAccessService.findUniqueJewelByLeague(UniqueJewel.ResolvedName.ANIMA_STONE.value, league);
    if (Objects.nonNull(animaStone)) {
      List<UniqueJewelEntity> ingredients = Stream.of(
          dataAccessService.findUniqueJewelByLeague(UniqueJewel.ResolvedName.PRIMORDIAL_EMINENCE.value, league),
          dataAccessService.findUniqueJewelByLeague(UniqueJewel.ResolvedName.PRIMORDIAL_HARMONY.value, league),
          dataAccessService.findUniqueJewelByLeague(UniqueJewel.ResolvedName.PRIMORDIAL_MIGHT.value, league)
      ).filter(Objects::nonNull).toList();
      AnimaStoneRecipe animaStoneRecipe =
          new AnimaStoneRecipe(Objects.requireNonNullElse(animaStone.getChaosEquivalent(), 0.0));
      boolean saved = vendorRecipeService.saveAnimaStoneRecipe(league, animaStoneRecipe, ingredients);
      if (saved) {
        log.info("Anima Stone recipe saved successfully for league '{}'", league.getName());
      }
    }
  }
}
