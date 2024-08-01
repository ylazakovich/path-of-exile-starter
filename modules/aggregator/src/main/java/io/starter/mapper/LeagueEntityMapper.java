package io.starter.mapper;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import io.starter.entity.LeagueEntity;
import io.starter.model.path_of_exile.League;
import org.springframework.stereotype.Service;

@Service
public class LeagueEntityMapper implements Function<List<League>, List<LeagueEntity>> {

  @Override
  public List<LeagueEntity> apply(List<League> data) {
    AtomicReference<Long> count = new AtomicReference<>((long) 1);
    return data.stream()
        .map(league -> {
              LeagueEntity leagueEntity = new LeagueEntity(league.getId());
              leagueEntity.setId(count.getAndSet(count.get() + 1));
              return leagueEntity;
        })
        .toList();
  }
}
