package io.starter.service;

import java.util.ArrayList;
import java.util.List;

import io.starter.entity.SkillEntity;
import io.starter.model.Lines;
import io.starter.model.Skill;
import io.starter.repo.SkillRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

  private final SkillsDAO skillsDAO;
  private final SkillRepo skillRepo;

  @Autowired
  public DatabaseService(SkillsDAO skillsDAO,
                         SkillRepo skillRepo) {
    this.skillsDAO = skillsDAO;
    this.skillRepo = skillRepo;
  }

  public void load(Lines<Skill> lines) {
    if (!lines.getLines().isEmpty()) {
      skillsDAO.saveAll(lines);
    }
  }

  public void update(Lines<Skill> lines) {
    List<SkillEntity> entitiesOnUpdate = skillRepo.findAll();
    entitiesOnUpdate.forEach(entity -> lines.getLines().stream()
        .filter(skill -> skill.getName().equals(entity.getName())
            && skill.getGemLevel() == entity.getGemLevel()
            && skill.getGemQuality() == entity.getGemQuality()
            && skill.isCorrupted() == entity.getCorrupted())
        .findFirst()
        .ifPresent(skill -> entity.setChaosValue(skill.getChaosValue()))
    );
    skillsDAO.saveAll(entitiesOnUpdate);
  }

  public void addNew(Lines<Skill> lines) {
    List<SkillEntity> allEntities = skillRepo.findAll();
    List<SkillEntity> entitiesOnAdding = new ArrayList<>();
    lines.getLines().stream()
        .filter(skill -> allEntities.stream()
            .noneMatch(entity -> entity.getName().equals(skill.getName()) &&
                entity.getGemLevel() == skill.getGemLevel() &&
                entity.getGemQuality() == skill.getGemQuality() &&
                entity.getVariant().equals(skill.getVariant()) &&
                entity.getCorrupted() == skill.isCorrupted()
            ))
        .forEach(skill -> {
          SkillEntity entity = new SkillEntity();
          entity.setName(skill.getName());
          entity.setVariant(skill.getVariant());
          entity.setGemLevel(skill.getGemLevel());
          entity.setGemQuality(skill.getGemQuality());
          entity.setCorrupted(skill.isCorrupted());
          entity.setChaosValue(skill.getChaosValue());
          entitiesOnAdding.add(entity);
        });
    skillsDAO.saveAll(entitiesOnAdding);
  }
}
