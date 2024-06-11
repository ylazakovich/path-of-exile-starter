package io.starter.telegram.dao;

import java.util.ArrayList;
import java.util.List;

import io.starter.telegram.entity.SkillEntity;
import io.starter.telegram.model.aggregator.Skill;
import io.starter.telegram.repo.SkillsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SkillsDao {

  private final SkillsRepository skillsRepository;

  @Autowired
  public SkillsDao(SkillsRepository skillsRepository) {
    this.skillsRepository = skillsRepository;
  }

  public List<Skill> findAll() {
    List<SkillEntity> all = skillsRepository.findAll(Sort.by(Sort.Direction.DESC, "chaosEquivalentProfit"));
    return all.stream().map(entity -> {
      Skill skill = new Skill();
      skill.setName(entity.getName());
      skill.setChaosEquivalentPrice(entity.getChaosEquivalentPrice());
      skill.setChaosEquivalentProfit(entity.getChaosEquivalentProfit());
      return skill;
    }).toList();
  }

  public void add(List<Skill> skills) {
    if (skillsRepository.findAll().isEmpty()) {
      final List<SkillEntity> entities = skills.stream()
          .map(skill -> {
            SkillEntity skillEntity = new SkillEntity();
            skillEntity.setName(skill.getName());
            skillEntity.setChaosEquivalentPrice(skill.getChaosEquivalentPrice());
            skillEntity.setChaosEquivalentProfit(skill.getChaosEquivalentProfit());
            return skillEntity;
          }).toList();
      skillsRepository.saveAll(entities);
    }
  }

  public void update(List<Skill> skills) {
    List<SkillEntity> entitiesOnUpdate = skillsRepository.findAll();
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
    skillsRepository.saveAll(entitiesOnUpdate);
  }

  public void addNew(List<Skill> skills) {
    List<SkillEntity> allEntities = skillsRepository.findAll();
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
    skillsRepository.saveAll(entitiesOnAdding);
  }
}
