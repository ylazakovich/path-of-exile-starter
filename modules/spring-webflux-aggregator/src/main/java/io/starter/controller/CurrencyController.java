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
@RequestMapping("/currencies")
@Log4j2
public class CurrencyController {

  private final DataAccessService dataAccessService;
  private final PoeNinjaService poeNinjaService;
  private final NinjaDataSyncService ninjaDataSyncService;

  public CurrencyController(DataAccessService dataAccessService, PoeNinjaService poeNinjaService,
                            NinjaDataSyncService ninjaDataSyncService) {
    this.dataAccessService = dataAccessService;
    this.poeNinjaService = poeNinjaService;
    this.ninjaDataSyncService = ninjaDataSyncService;
  }

  @PostMapping("/load")
  public void loadCurrencies() {
    dataAccessService.findLeagues().forEach(this::loadCurrencies);
  }

  @Scheduled(cron = ScheduleConfig.A8R_ADD_CRON)
  public void updateCurrencies() {
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getRates(league.getName())
            .subscribe(response -> {
              ninjaDataSyncService.updateCurrencies(response.getBody(), league);
              log.info("{} - Currency - Schedule updating", league.getName());
            })
    );
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
}
