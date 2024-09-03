package io.starter.service;

import java.util.Objects;

import io.starter.entity.LeagueEntity;
import io.starter.entity.RateEntity;
import io.starter.repo.RatesRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RateService {

  private final RatesRepository ratesRepository;

  public RateService(RatesRepository ratesRepository) {
    this.ratesRepository = ratesRepository;
  }

  @Transactional
  public double convertChaosToDivineEquivalent(double chaosEquivalent,
                                               LeagueEntity league) {
    final String item = "Divine Orb";
    RateEntity entity = ratesRepository.findByNameAndLeagueId(item, league);
    if (Objects.isNull(entity)) {
      return 0;
    }
    return chaosEquivalent / entity.getChaosEquivalent();
  }
}

