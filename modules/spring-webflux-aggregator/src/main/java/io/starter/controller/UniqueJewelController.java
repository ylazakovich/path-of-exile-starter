package io.starter.controller;

import io.starter.config.ScheduleConfig;
import io.starter.entity.LeagueEntity;
import io.starter.service.DataAccessService;
import io.starter.service.NinjaDataSyncService;
import io.starter.service.PoeNinjaService;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unique-jewels")
@Log4j2
public class UniqueJewelController {

  private final DataAccessService dataAccessService;
  private final PoeNinjaService poeNinjaService;
  private final NinjaDataSyncService ninjaDataSyncService;

  public UniqueJewelController(DataAccessService dataAccessService,
                               PoeNinjaService poeNinjaService,
                               NinjaDataSyncService ninjaDataSyncService) {
    this.dataAccessService = dataAccessService;
    this.poeNinjaService = poeNinjaService;
    this.ninjaDataSyncService = ninjaDataSyncService;
  }

  @PostMapping("/load")
  public void loadUniqueJewels() {
    dataAccessService.findLeagues().forEach(this::loadUniqueJewels);
  }

  private void loadUniqueJewels(LeagueEntity league) {
    poeNinjaService.getUniqueJewels(league.getName())
        .subscribe(response -> {
          ninjaDataSyncService.loadUniqueJewels(response.getBody(), league);
          log.info("{} - Unique Jewel - Loaded {} units",
              league.getName(),
              dataAccessService.findUniqueJewelsByLeague(league).size());
        });
  }

  @Scheduled(cron = ScheduleConfig.A8R_UPDATE_CRON)
  public void updateUniqueJewels() {
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getUniqueJewels(league.getName())
            .subscribe(response -> {
              ninjaDataSyncService.updateJewels(response.getBody(), league);
              log.info("{} - Unique Jewel - Schedule updating", league.getName());
            })
    );
  }

  @Scheduled(cron = ScheduleConfig.A8R_ADD_CRON)
  public void addNewUniqueJewels() {
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getUniqueJewels(league.getName())
            .subscribe(response -> {
              ninjaDataSyncService.addNewJewels(response.getBody(), league);
              log.info("{} - Unique Jewel - Schedule adding new", league.getName());
            })
    );
  }
}
