package io.starter.service;

import io.starter.entity.LeagueEntity;

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
    return dataAccessService.findRateByNameAndLeague("Divine Orb", league)
        .map(rate -> chaosEquivalent / rate.getChaosEquivalent())
        .orElse(0.0);
  }
}

