package io.starter.service;

import java.util.List;

import io.starter.entity.SkillEntity;
import io.starter.mapper.SkillEntityMapper;
import io.starter.model.Lines;
import io.starter.model.Skill;
import io.starter.repo.SkillRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillsDAO {

  private final SkillRepo skillRepo;
  // TODO: need parametrized entity
  private final SkillEntityMapper entityMapper;

  @Autowired
  public SkillsDAO(SkillRepo skillRepo,
                   SkillEntityMapper entityMapper) {
    this.skillRepo = skillRepo;
    this.entityMapper = entityMapper;
  }

  public List<SkillEntity> findAll() {
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
