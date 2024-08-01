package io.starter.service;

import java.util.List;

import io.starter.dao.LeaguesDao;
import io.starter.model.path_of_exile.League;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabasePathOfExileService {

  private final LeaguesDao leaguesDao;

  @Autowired
  public DatabasePathOfExileService(LeaguesDao leaguesDao) {
    this.leaguesDao = leaguesDao;
  }

  public void load(List<League> leagues) {
    leaguesDao.saveAll(leagues);
  }
}
