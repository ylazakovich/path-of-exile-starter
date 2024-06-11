package io.starter.service;

import java.util.List;

import io.starter.entity.SkillEntity;
import io.starter.mapper.SkillEntityMapper;
import io.starter.model.Lines;
import io.starter.model.Skill;
import io.starter.repo.SkillsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillsDao {

  private final SkillsRepository skillsRepository;
  // TODO: need parametrized entity
  private final SkillEntityMapper entityMapper;

  @Autowired
  public SkillsDao(SkillsRepository skillsRepository,
                   SkillEntityMapper entityMapper) {
    this.skillsRepository = skillsRepository;
    this.entityMapper = entityMapper;
  }

  public void saveAll(Lines<Skill> data) {
    List<SkillEntity> entityList = entityMapper.apply(data);
    skillsRepository.saveAll(entityList);
  }

  public void saveAll(List<SkillEntity> entities) {
    skillsRepository.saveAll(entities);
  }
}
