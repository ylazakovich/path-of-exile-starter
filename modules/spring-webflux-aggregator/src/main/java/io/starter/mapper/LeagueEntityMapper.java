package io.starter.mapper;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import io.starter.entity.LeagueEntity;
import io.starter.model.path_of_exile.League;
import io.starter.service.DataAccessService;

import org.springframework.stereotype.Service;

@Service
public class LeagueEntityMapper implements Function<List<League>, List<LeagueEntity>> {

  private final DataAccessService dataAccessService;

  public LeagueEntityMapper(DataAccessService dataAccessService) {
    this.dataAccessService = dataAccessService;
  }

  @Override
  public List<LeagueEntity> apply(List<League> data) {
    if (data == null || data.isEmpty()) {
      return List.of();
    }
    return data.stream()
        .map(league -> {
          LeagueEntity leagueEntity = Optional
              .ofNullable(dataAccessService.findLeagueByName(league.getId()))
              .orElseGet(LeagueEntity::new);
          leagueEntity.setName(league.getId());
          return leagueEntity;
        })
        .toList();
  }
}
