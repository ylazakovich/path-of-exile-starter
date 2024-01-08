package io.automation.service;

import java.util.List;

import io.automation.entity.SkillGemEntity;
import io.automation.mapper.SkillGemEntityMapper;
import io.automation.model.Lines;
import io.automation.model.SkillGem;
import io.automation.repo.GemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillGemService {

  private final GemRepo gemRepo;
  // TODO: need parametrized entity
  private final SkillGemEntityMapper entityMapper;

  @Autowired
  public SkillGemService(GemRepo gemRepo,
                         SkillGemEntityMapper entityMapper) {
    this.gemRepo = gemRepo;
    this.entityMapper = entityMapper;
  }

  public List<SkillGemEntity> findAllGems() {
    return gemRepo.findAll();
  }

  public void saveAll(Lines<SkillGem> data) {
    List<SkillGemEntity> entityList = entityMapper.apply(data);
    gemRepo.saveAll(entityList);
  }

  public void saveAll(List<SkillGemEntity> entities) {
    gemRepo.saveAll(entities);
  }

  public void deleteAll() {
    gemRepo.truncateTable();
  }
}
