package io.starter.mapper;

import java.util.List;
import java.util.function.Function;

import io.starter.entity.LeagueEntity;
import io.starter.entity.RateEntity;
import io.starter.model.ninja.Currency;
import io.starter.model.ninja.Lines;

import org.springframework.stereotype.Service;

@Service
public class RateEntityMapper implements Function<Lines<Currency>, List<RateEntity>> {

  @Override
  public List<RateEntity> apply(Lines<Currency> data) {
    return data.getLines().stream()
        .map(skill -> new RateEntity(
            new LeagueEntity(),
            skill.getName(),
            skill.getChaosEquivalent()))
        .toList();
  }
}
