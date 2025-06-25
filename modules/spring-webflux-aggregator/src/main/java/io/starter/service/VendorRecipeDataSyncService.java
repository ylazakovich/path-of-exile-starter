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

  public void load(VendorRecipeEntity entity, LeagueEntity league) {
    dataAccessService.findVendorRecipeByNameAndLeague(entity.getName(), league)
        .orElseGet(() -> {
          dataAccessService.saveVendorRecipe(entity);
          return null;
        });
  }
}
