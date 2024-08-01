package io.starter.dao;

import java.util.List;

import io.starter.entity.LeagueEntity;
import io.starter.mapper.LeagueEntityMapper;
import io.starter.model.path_of_exile.League;
import io.starter.repo.LeaguesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeaguesDao {

  private final LeaguesRepository repo;
  private final LeagueEntityMapper mapper;

  @Autowired
  public LeaguesDao(LeaguesRepository repo,
                    LeagueEntityMapper mapper) {
    this.repo = repo;
    this.mapper = mapper;
  }

  public void saveAll(List<League> data) {
    List<LeagueEntity> entityList = mapper.apply(data);
    repo.saveAll(entityList);
  }
}
