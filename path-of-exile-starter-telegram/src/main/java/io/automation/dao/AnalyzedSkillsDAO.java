package io.automation.dao;

import java.util.List;

import io.automation.aggregator.model.Skill;
import io.automation.entity.AnalyzedSkillEntity;
import io.automation.repo.AnalyzedSkillsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyzedSkillsDAO {

  private final AnalyzedSkillsRepository analyzedSkillsRepository;

  @Autowired
  public AnalyzedSkillsDAO(AnalyzedSkillsRepository analyzedSkillsRepository) {
    this.analyzedSkillsRepository = analyzedSkillsRepository;
  }

  public List<Skill> findAll() {
    List<AnalyzedSkillEntity> all = analyzedSkillsRepository.findAll();
    return all.stream().map(entity -> {
      Skill skill = new Skill();
      skill.setName(entity.getName());
      skill.setCraftCost(entity.getCraftCost());
      skill.setProfit(entity.getProfit());
      return skill;
    }).toList();
  }

  public void updateAll(List<Skill> skills) {
    analyzedSkillsRepository.truncateTable();
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
