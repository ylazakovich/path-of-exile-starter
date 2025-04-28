package io.starter.telegram.dao;

import java.util.ArrayList;
import java.util.List;

import io.starter.telegram.entity.LeagueEntity;
import io.starter.telegram.entity.SkillEntity;
import io.starter.telegram.model.aggregator.Skill;
import io.starter.telegram.repo.SkillRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SkillDao {

  private final SkillRepository skillRepository;

  @Autowired
  public SkillDao(SkillRepository skillRepository) {
    this.skillRepository = skillRepository;
  }

  public List<Skill> readAll(LeagueEntity league) {
    final String column = "chaosEquivalentProfit";
    List<SkillEntity> all = skillRepository.findAllByLeagueId(league, Sort.by(Sort.Direction.DESC, column));
    return all.stream().map(entity -> {
      Skill skill = new Skill();
      skill.setName(entity.getName());
//      skill.setChaosEquivalentPrice(entity.getChaosEquivalentPrice());
//      skill.setChaosEquivalentProfit(entity.getChaosEquivalentProfit());
      return skill;
    }).toList();
  }

  public void add(LeagueEntity league, List<Skill> skills) {
    if (skillRepository.findAllByLeagueId(league).isEmpty()) {
      final List<SkillEntity> entities = skills.stream()
          .map(skill -> new SkillEntity(
              league,
              skill.getName()
//              skill.getChaosEquivalentPrice(),
//              skill.getChaosEquivalentProfit()
          )).toList();
      skillRepository.saveAll(entities);
    }
  }

  public void update(LeagueEntity league, List<Skill> skills) {
    List<SkillEntity> entitiesOnUpdate = skillRepository.findAllByLeagueId(league);
    skills.forEach(skill ->
        entitiesOnUpdate.stream()
            .filter(entity -> entity.getName().equals(skill.getName()))
            .findFirst()
            .ifPresent(matchedEntity -> {
//                  matchedEntity.setChaosEquivalentPrice(skill.getChaosEquivalentPrice());
//                  matchedEntity.setChaosEquivalentProfit(skill.getChaosEquivalentProfit());
                }
            )
    );
    skillRepository.saveAll(entitiesOnUpdate);
  }

  public void addNew(LeagueEntity league, List<Skill> skills) {
    List<SkillEntity> allEntities = skillRepository.findAllByLeagueId(league);
    List<SkillEntity> entitiesOnAdding = new ArrayList<>();
    skills.stream()
        .filter(skill -> allEntities.stream().noneMatch(entity -> entity.getName().equals(skill.getName())))
        .forEach(skill -> {
          SkillEntity skillEntity = new SkillEntity(
              league,
              skill.getName()
//              skill.getChaosEquivalentPrice(),
//              skill.getChaosEquivalentProfit()
          );
          entitiesOnAdding.add(skillEntity);
        });
    skillRepository.saveAll(entitiesOnAdding);
  }
}
