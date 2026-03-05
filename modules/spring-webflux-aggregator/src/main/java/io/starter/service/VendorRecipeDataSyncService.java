package io.starter.service;

import io.starter.entity.LeagueEntity;
import io.starter.entity.VendorRecipeEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VendorRecipeDataSyncService {

  private final DataAccessService dataAccessService;

  public void upsert(VendorRecipeEntity entity, LeagueEntity league) {
    dataAccessService.findVendorRecipeByNameAndLeague(entity.getName(), league)
        .ifPresentOrElse(existing -> {
          existing.setChaosEquivalentPrice(entity.getChaosEquivalentPrice());
          existing.setChaosEquivalentProfit(entity.getChaosEquivalentProfit());
          dataAccessService.saveVendorRecipe(existing);
        }, () -> dataAccessService.saveVendorRecipe(entity));
  }

  public void deleteByNameAndLeague(String name, LeagueEntity league) {
    dataAccessService.deleteVendorRecipeByNameAndLeague(name, league);
  }
}
