package io.starter.service;

import java.util.Objects;

import io.starter.entity.LeagueEntity;
import io.starter.entity.RateEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateService {

  private final DataAccessService dataAccessService;

  @Autowired
  public RateService(DataAccessService dataAccessService) {
    this.dataAccessService = dataAccessService;
  }

  public double toDivineEquivalent(double chaosEquivalent,
                                   LeagueEntity league) {
    final String item = "Divine Orb";
    RateEntity entity = dataAccessService.findRateByNameAndLeague(item, league);
    if (Objects.isNull(entity)) {
      return 0;
    }
    return chaosEquivalent / entity.getChaosEquivalent();
  }
}

