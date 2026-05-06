package io.starter.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import io.starter.config.ScheduleConfig;
import io.starter.entity.LeagueEntity;
import io.starter.model.ninja.Currency;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.UniqueJewel;
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
    loadCombinedRatesForLeague(league)
        .subscribe(mergedRates -> {
          ninjaDataSyncService.loadCurrency(mergedRates, league);
          log.info("{} - Currency+Divination - Loaded {} units",
              league.getName(),
              dataAccessService.findRatesByLeague(league).size());
        });
  }

  private Mono<Void> updateCurrenciesForLeague(LeagueEntity league) {
    return loadCombinedRatesForLeague(league)
        .doOnNext(mergedRates -> {
          ninjaDataSyncService.updateCurrencies(mergedRates, league);
          log.info("{} - Currency+Divination - Schedule updating", league.getName());
        })
        .then()
        .onErrorResume(e -> {
          log.error("{} - Currency+Divination - Schedule updating failed", league.getName(), e);
          return Mono.empty();
        });
  }

  private Mono<Lines<Currency>> loadCombinedRatesForLeague(LeagueEntity league) {
    return Mono.zip(
        poeNinjaService.getRates(league.getName()),
        poeNinjaService.getUniqueItems(league.getName(), "DivinationCard")
    ).map(tuple -> mergeRates(tuple.getT1().getBody(), tuple.getT2().getBody()));
  }

  private Lines<Currency> mergeRates(Lines<Currency> rates, Lines<UniqueJewel> divinationCards) {
    Map<String, Double> merged = new LinkedHashMap<>();
    if (rates != null && rates.getLines() != null) {
      rates.getLines().forEach(currency -> mergeRate(merged, currency.getName(), currency.getChaosEquivalent()));
    }
    if (divinationCards != null && divinationCards.getLines() != null) {
      divinationCards.getLines().forEach(card -> mergeRate(merged, card.getName(), card.getChaosEquivalent()));
    }
    List<Currency> lines = merged.entrySet().stream()
        .map(entry -> Currency.builder().name(entry.getKey()).chaosEquivalent(entry.getValue()).build())
        .toList();
    return new Lines<>(lines);
  }

  private void mergeRate(Map<String, Double> target, String name, double chaosEquivalent) {
    if (name == null || name.isBlank() || chaosEquivalent <= 0) {
      return;
    }
    target.merge(name, chaosEquivalent, Math::min);
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
