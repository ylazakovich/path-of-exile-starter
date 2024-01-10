package io.automation.service;

import java.util.List;

import io.automation.entity.SkillEntity;
import io.automation.mapper.SkillEntityMapper;
import io.automation.model.Lines;
import io.automation.model.Skill;
import io.automation.repo.SkillRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillsService {

  private final SkillRepo skillRepo;
  // TODO: need parametrized entity
  private final SkillEntityMapper entityMapper;

  @Autowired
  public SkillsService(SkillRepo skillRepo,
                       SkillEntityMapper entityMapper) {
    this.skillRepo = skillRepo;
    this.entityMapper = entityMapper;
  }

  public List<SkillEntity> findAllSkills() {
    return skillRepo.findAll();
  }

  public void saveAll(Lines<Skill> data) {
    List<SkillEntity> entityList = entityMapper.apply(data);
    skillRepo.saveAll(entityList);
  }

  public void saveAll(List<SkillEntity> entities) {
    skillRepo.saveAll(entities);
  }

  public void clearTable() {
    skillRepo.truncateTable();
  }
}
