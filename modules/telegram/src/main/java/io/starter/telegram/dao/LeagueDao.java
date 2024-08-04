package io.starter.telegram.dao;

import java.util.List;

import io.starter.telegram.entity.LeagueEntity;
import io.starter.telegram.model.aggregator.League;
import io.starter.telegram.repo.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeagueDao {

  private final LeagueRepository repo;

  @Autowired
  public LeagueDao(LeagueRepository leagueRepository) {
    this.repo = leagueRepository;
  }

  public List<LeagueEntity> readAll() {
    return repo.findAll();
  }

  public void update(List<League> leagues) {
    final List<LeagueEntity> entities = leagues.stream()
        .map(league -> new LeagueEntity(league.getName()))
        .toList();
    repo.saveAll(entities);
  }
}
