package io.starter.service;

import java.util.List;

import io.starter.entity.LeagueEntity;
import io.starter.entity.ProcessedSkillEntity;
import io.starter.model.aggregator.Skill;
import io.starter.repo.ProcessedSkillsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DataAccessService {

  private final ProcessedSkillsRepository processedSkillsRepository;

  @Autowired
  public DataAccessService(ProcessedSkillsRepository processedSkillsRepository) {
    this.processedSkillsRepository = processedSkillsRepository;
  }

  public List<Skill> findAllSkills(LeagueEntity league) {
    final String column = "chaosEquivalentProfit";
    List<ProcessedSkillEntity> all = processedSkillsRepository.findAllByLeagueId(league, Sort.by(Sort.Direction.DESC, column));
    return all.stream().map(entity -> {
      Skill skill = new Skill();
      skill.setName(entity.getSkill().getName());
      skill.setChaosEquivalentPrice(entity.getChaosEquivalentPrice());
      skill.setChaosEquivalentProfit(entity.getChaosEquivalentProfit());
      return skill;
    }).toList();
  }
}
