package io.starter.dao;

import java.util.List;

import io.starter.entity.LeagueEntity;
import io.starter.model.aggregator.League;
import io.starter.repo.LeagueRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeagueDao {

  private final LeagueRepository repo;

  @Autowired
  public LeagueDao(LeagueRepository leagueRepository) {
    this.repo = leagueRepository;
  }

  public List<LeagueEntity> selectAll() {
    return repo.findAll();
  }

  public void save(List<League> leagues) {
    final List<LeagueEntity> entities = leagues.stream()
        .map(league -> new LeagueEntity(league.getName()))
        .toList();
    repo.saveAll(entities);
  }

  public void save(LeagueEntity entity) {
    repo.save(entity);
  }

  public void saveIfAbsent(List<League> leagues) {
    leagues.forEach(league -> {
      if (repo.findById(league.getId()).isEmpty()) {
        save(new LeagueEntity(league.getName()));
      }
    });
  }
}
