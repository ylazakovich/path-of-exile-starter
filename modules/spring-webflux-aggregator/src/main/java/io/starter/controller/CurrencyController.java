package io.starter.controller;

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
@RequestMapping("/currencies")
@RequiredArgsConstructor
@Log4j2
public class CurrencyController {

  private final AtomicBoolean updateCurrenciesRunning = new AtomicBoolean(false);
  private final DataAccessService dataAccessService;
  private final PoeNinjaService poeNinjaService;
  private final NinjaDataSyncService ninjaDataSyncService;

  @PostMapping("/load")
  public void loadCurrencies() {
    dataAccessService.findLeagues().forEach(this::loadCurrencies);
  }

  @Scheduled(cron = ScheduleConfig.A8R_CURRENCIES_UPDATE_CRON)
  public void updateCurrencies() {
    runScheduledJob("currencies-update", updateCurrenciesRunning, () ->
        Flux.fromIterable(dataAccessService.findLeagues())
            .concatMap(this::updateCurrenciesForLeague)
            .then());
  }

  private void loadCurrencies(LeagueEntity league) {
    poeNinjaService.getRates(league.getName())
        .subscribe(response -> {
          ninjaDataSyncService.loadCurrency(response.getBody(), league);
          log.info("{} - Currency - Loaded {} units",
              league.getName(),
              dataAccessService.findRatesByLeague(league).size());
        });
  }

  private Mono<Void> updateCurrenciesForLeague(LeagueEntity league) {
    return poeNinjaService.getRates(league.getName())
        .doOnNext(response -> {
          ninjaDataSyncService.updateCurrencies(response.getBody(), league);
          log.info("{} - Currency - Schedule updating", league.getName());
        })
        .then()
        .onErrorResume(e -> {
          log.error("{} - Currency - Schedule updating failed", league.getName(), e);
          return Mono.empty();
        });
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
