package io.starter.mapper;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import io.starter.entity.LeagueEntity;
import io.starter.model.path_of_exile.League;
import io.starter.repo.LeaguesRepository;

import org.springframework.stereotype.Service;

@Service
public class LeagueEntityMapper implements Function<List<League>, List<LeagueEntity>> {

  private final LeaguesRepository repo;

  public LeagueEntityMapper(LeaguesRepository repo) {
    this.repo = repo;
  }

  @Override
  public List<LeagueEntity> apply(List<League> data) {
    return data.stream()
        .map(league -> {
          LeagueEntity leagueEntity = Optional.ofNullable(repo.findByName(league.getId())).orElseGet(LeagueEntity::new);
          leagueEntity.setName(league.getId());
          return leagueEntity;
        })
        .toList();
  }
}
