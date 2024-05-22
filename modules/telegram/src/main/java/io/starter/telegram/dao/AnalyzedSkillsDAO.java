package io.starter.telegram.dao;

import java.util.List;
import java.util.Objects;

import io.starter.aggregator.model.Skill;
import io.starter.telegram.entity.AnalyzedSkillEntity;
import io.starter.telegram.repo.AnalyzedSkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AnalyzedSkillsDAO {

  private final AnalyzedSkillsRepository analyzedSkillsRepository;

  @Autowired
  public AnalyzedSkillsDAO(AnalyzedSkillsRepository analyzedSkillsRepository) {
    this.analyzedSkillsRepository = analyzedSkillsRepository;
  }

  public List<Skill> findAll() {
    List<AnalyzedSkillEntity> all = analyzedSkillsRepository.findAll(Sort.by(Sort.Direction.DESC, "profit"));
    return all.stream().map(entity -> {
      Skill skill = new Skill();
      skill.setName(entity.getName());
      skill.setCraftCost(entity.getCraftCost());
      skill.setProfit(entity.getProfit());
      return skill;
    }).toList();
  }

  public void add(List<Skill> skills) {
    if (skills.isEmpty()) {
      final List<AnalyzedSkillEntity> entities = skills.stream()
          .map(skill -> {
            AnalyzedSkillEntity analyzedSkillEntity = new AnalyzedSkillEntity();
            analyzedSkillEntity.setName(skill.getName());
            analyzedSkillEntity.setCraftCost(skill.getCraftCost());
            analyzedSkillEntity.setProfit(skill.getProfit());
            return analyzedSkillEntity;
          }).toList();
      analyzedSkillsRepository.saveAll(entities);
    }
  }

  public void update(List<Skill> skillsAfter) {
    List<AnalyzedSkillEntity> skillsBefore = analyzedSkillsRepository.findAll();
    skillsAfter.forEach(skillAfter ->
        skillsBefore.stream()
            .filter(skillBefore -> skillBefore.getName().equals(skillAfter.getName()))
            .findFirst()
            .ifPresent(matchedEntity -> {
                  matchedEntity.setCraftCost(skillAfter.getCraftCost());
                  matchedEntity.setProfit(skillAfter.getProfit());
                }
            )
    );
    analyzedSkillsRepository.saveAll(skillsBefore);
  }
}
