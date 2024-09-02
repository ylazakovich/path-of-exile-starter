package io.starter.dao;

import java.util.List;
import java.util.Optional;

import io.starter.entity.SkillEntity;
import io.starter.mapper.SkillEntityMapper;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;
import io.starter.repo.LeaguesRepository;
import io.starter.repo.SkillsRepository;
import io.starter.service.RateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SkillsDao {

  private final RateService rateService;
  private final SkillsRepository repo;
  private final LeaguesRepository leagueRepo;
  private final SkillEntityMapper mapper;

  @Autowired
  public SkillsDao(RateService rateService,
                   SkillsRepository repo,
                   LeaguesRepository leagueRepo,
                   SkillEntityMapper mapper) {
    this.rateService = rateService;
    this.repo = repo;
    this.leagueRepo = leagueRepo;
    this.mapper = mapper;
  }

  @Transactional
  public void saveAll(Lines<Skill> data, Long id) {
    leagueRepo.findById(id)
        .flatMap(league -> {
          List<SkillEntity> entityList = mapper.apply(data);
          entityList.forEach(entity -> {
            entity.setLeagueId(league);
            entity.setDivineEquivalent(rateService.convertChaosToDivineEquivalent(entity.getChaosEquivalent(), league));
          });
          repo.saveAll(entityList);
          return Optional.empty();
        });
  }

  public void saveAll(List<SkillEntity> entities, Long id) {
    leagueRepo.findById(id).flatMap(
        league -> {
          entities.forEach(entity -> {
            entity.setLeagueId(leagueRepo.findById(id).orElseThrow());
            entity.setDivineEquivalent(rateService.convertChaosToDivineEquivalent(entity.getChaosEquivalent(), league));
          });
          repo.saveAll(entities);
          return Optional.empty();
        }
    );
  }
}
