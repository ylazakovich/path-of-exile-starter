package io.starter.telegram.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.starter.telegram.entity.LeagueEntity;
import io.starter.telegram.entity.SkillEntity;
import io.starter.telegram.model.aggregator.Skill;
import io.starter.telegram.repo.LeagueRepository;
import io.starter.telegram.repo.SkillRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SkillDao {

  private final SkillRepository skillRepository;
  private final LeagueRepository leagueRepository;

  @Autowired
  public SkillDao(SkillRepository skillRepository,
                  LeagueRepository leagueRepository) {
    this.skillRepository = skillRepository;
    this.leagueRepository = leagueRepository;
  }

  public List<Skill> readAll() {
    List<SkillEntity> all = skillRepository.findAll(Sort.by(Sort.Direction.DESC, "chaosEquivalentProfit"));
    return all.stream().map(entity -> {
      Skill skill = new Skill();
      skill.setName(entity.getName());
      skill.setChaosEquivalentPrice(entity.getChaosEquivalentPrice());
      skill.setChaosEquivalentProfit(entity.getChaosEquivalentProfit());
      return skill;
    }).toList();
  }

  public void add(List<Skill> skills) {
    leagueRepository.findAll()
        .forEach(league -> {
          if (skillRepository.findAll().isEmpty()) {
            final List<SkillEntity> entities = skills.stream()
                .map(skill -> {
                  SkillEntity skillEntity = new SkillEntity();
                  skillEntity.setLeagueId(league); // TODO: Finally need to fetch league
                  skillEntity.setName(skill.getName());
                  skillEntity.setChaosEquivalentPrice(skill.getChaosEquivalentPrice());
                  skillEntity.setChaosEquivalentProfit(skill.getChaosEquivalentProfit());
                  return skillEntity;
                }).toList();
            skillRepository.saveAll(entities);
          }
        }
    );
  }

  public void update(List<Skill> skills) {
    List<SkillEntity> entitiesOnUpdate = skillRepository.findAll();
    skills.forEach(skill ->
        entitiesOnUpdate.stream()
            .filter(entity -> entity.getName().equals(skill.getName()))
            .findFirst()
            .ifPresent(matchedEntity -> {
                  matchedEntity.setChaosEquivalentPrice(skill.getChaosEquivalentPrice());
                  matchedEntity.setChaosEquivalentProfit(skill.getChaosEquivalentProfit());
                }
            )
    );
    skillRepository.saveAll(entitiesOnUpdate);
  }

  public void addNew(List<Skill> skills) {
    List<SkillEntity> allEntities = skillRepository.findAll();
    List<SkillEntity> entitiesOnAdding = new ArrayList<>();
    skills.stream()
        .filter(skill -> allEntities.stream().noneMatch(entity -> entity.getName().equals(skill.getName())))
        .forEach(skill -> {
          SkillEntity skillEntity = new SkillEntity();
          skillEntity.setName(skill.getName());
          skillEntity.setChaosEquivalentPrice(skill.getChaosEquivalentPrice());
          skillEntity.setChaosEquivalentProfit(skill.getChaosEquivalentProfit());
          entitiesOnAdding.add(skillEntity);
        });
    skillRepository.saveAll(entitiesOnAdding);
  }
}
