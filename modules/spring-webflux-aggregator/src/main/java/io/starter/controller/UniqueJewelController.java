package io.starter.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
  private final AtomicBoolean updateUniqueJewelsRunning = new AtomicBoolean(false);
  private final AtomicBoolean addUniqueJewelsRunning = new AtomicBoolean(false);
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
    runScheduledJob("unique-jewels-update", updateUniqueJewelsRunning, () -> syncUniqueJewelsByType(true));
  }

  @Scheduled(cron = ScheduleConfig.A8R_UNIQUE_JEWELS_ADD_CRON)
  public void addNewUniqueJewels() {
    runScheduledJob("unique-jewels-add-new", addUniqueJewelsRunning, () -> syncUniqueJewelsByType(false));
  }

  private Mono<Void> syncUniqueItemsReactive(LeagueEntity league, String type, boolean shouldUpdate) {
    return poeNinjaService.getUniqueItems(league.getName(), type)
        .doOnNext(response -> {
          if (shouldUpdate) {
            ninjaDataSyncService.updateJewels(response.getBody(), league);
          }
          ninjaDataSyncService.addNewJewels(response.getBody(), league);
          log.info("{} - {} - Synced {} units",
              league.getName(),
              type,
              dataAccessService.findUniqueJewelsByLeague(league).size());
        })
        .then()
        .onErrorResume(e -> {
          log.error("{} - {} - Unique items sync failed", league.getName(), type, e);
          return Mono.empty();
        });
  }

  private Mono<Void> syncUniqueJewelsByType(boolean shouldUpdate) {
    return Flux.fromIterable(dataAccessService.findLeagues())
        .concatMap(league ->
            Flux.fromIterable(UNIQUE_ITEM_TYPES)
                .concatMap(type -> syncUniqueItemsReactive(league, type, shouldUpdate)))
        .then();
  }

  private void runScheduledJob(String jobName, AtomicBoolean lock, Supplier<Mono<Void>> scheduledAction) {
    if (!lock.compareAndSet(false, true)) {
      log.warn("Skipping '{}' run because previous execution is still in progress", jobName);
      return;
    }
    scheduledAction.get()
        .doFinally(signalType -> lock.set(false))
        .subscribe(
            unused -> { },
            error -> log.error("Job '{}' failed", jobName, error)
        );
  }
}
