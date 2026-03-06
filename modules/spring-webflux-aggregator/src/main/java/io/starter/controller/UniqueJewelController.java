package io.starter.controller;

import java.util.List;

import io.starter.config.ScheduleConfig;
import io.starter.entity.LeagueEntity;
import io.starter.service.DataAccessService;
import io.starter.service.NinjaDataSyncService;
import io.starter.service.PoeNinjaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unique-jewels")
@RequiredArgsConstructor
@Log4j2
public class UniqueJewelController {

  private static final List<String> UNIQUE_ITEM_TYPES = List.of(
      "UniqueJewel",
      "UniqueArmour",
      "UniqueAccessory",
      "UniqueWeapon",
      "UniqueFlask",
      "UniqueMap"
  );
  private final DataAccessService dataAccessService;
  private final PoeNinjaService poeNinjaService;
  private final NinjaDataSyncService ninjaDataSyncService;

  @PostMapping("/load")
  public void loadUniqueJewels() {
    dataAccessService.findLeagues().forEach(league ->
        UNIQUE_ITEM_TYPES.forEach(type -> syncUniqueItems(league, type, true)));
  }

  private void syncUniqueItems(LeagueEntity league, String type, boolean shouldUpdate) {
    poeNinjaService.getUniqueItems(league.getName(), type)
        .subscribe(response -> {
          if (shouldUpdate) {
            ninjaDataSyncService.updateJewels(response.getBody(), league);
          }
          ninjaDataSyncService.addNewJewels(response.getBody(), league);
          log.info("{} - {} - Synced {} units",
              league.getName(),
              type,
              dataAccessService.findUniqueJewelsByLeague(league).size());
        });
  }

  @Scheduled(cron = ScheduleConfig.A8R_UNIQUE_JEWELS_UPDATE_CRON)
  public void updateUniqueJewels() {
    dataAccessService.findLeagues().forEach(league ->
        UNIQUE_ITEM_TYPES.forEach(type -> syncUniqueItems(league, type, true))
    );
  }

  @Scheduled(cron = ScheduleConfig.A8R_UNIQUE_JEWELS_ADD_CRON)
  public void addNewUniqueJewels() {
    dataAccessService.findLeagues().forEach(league ->
        UNIQUE_ITEM_TYPES.forEach(type -> syncUniqueItems(league, type, false))
    );
  }
}
