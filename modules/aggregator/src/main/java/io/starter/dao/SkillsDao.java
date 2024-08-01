package io.starter.dao;

import java.util.List;
import java.util.Optional;

import io.starter.entity.LeagueEntity;
import io.starter.entity.SkillEntity;
import io.starter.mapper.SkillEntityMapper;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;
import io.starter.repo.LeaguesRepository;
import io.starter.repo.SkillsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillsDao {

  private final SkillsRepository repo;
  private final LeaguesRepository leagueRepo;
  // TODO: need parametrized entity
  private final SkillEntityMapper mapper;

  @Autowired
  public SkillsDao(SkillsRepository repo,
                   LeaguesRepository leagueRepo,
                   SkillEntityMapper mapper) {
    this.repo = repo;
    this.leagueRepo = leagueRepo;
    this.mapper = mapper;
  }

  public void saveAll(Lines<Skill> data) {
    List<SkillEntity> entityList = mapper.apply(data);
    repo.saveAll(entityList);
  }

  public void saveAll(List<SkillEntity> entities) {
    Optional<LeagueEntity> leagueEntity = leagueRepo.findById(1L);
    // TODO: Need implement dynamically read league
    entities.forEach(entity -> entity.setLeagueId(leagueEntity.orElseThrow()));
    repo.saveAll(entities);
  }
}
