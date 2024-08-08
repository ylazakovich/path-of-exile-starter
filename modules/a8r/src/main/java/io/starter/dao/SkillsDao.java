package io.starter.dao;

import java.util.List;
import java.util.Optional;

import io.starter.entity.SkillEntity;
import io.starter.mapper.SkillEntityMapper;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;
import io.starter.repo.LeaguesRepository;
import io.starter.repo.SkillsRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillsDao {

  private final SkillsRepository repo;
  private final LeaguesRepository leagueRepo;
  private final SkillEntityMapper mapper;

  @Autowired
  public SkillsDao(SkillsRepository repo,
                   LeaguesRepository leagueRepo,
                   SkillEntityMapper mapper) {
    this.repo = repo;
    this.leagueRepo = leagueRepo;
    this.mapper = mapper;
  }

  @Transactional
  public void saveAll(Lines<Skill> data, Long id) {
    leagueRepo.findById(id)
        .flatMap(league -> {
          List<SkillEntity> entityList = mapper.apply(data);
          entityList.forEach(entity -> entity.setLeagueId(league));
          repo.saveAll(entityList);
          return Optional.empty();
        });
  }

  public void saveAll(List<SkillEntity> entities, Long id) {
    entities.forEach(entity -> entity.setLeagueId(leagueRepo.findById(id).orElseThrow()));
    repo.saveAll(entities);
  }
}
