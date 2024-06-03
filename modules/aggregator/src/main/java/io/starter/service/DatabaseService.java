package io.starter.service;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.repository.util.ClassUtils.ifPresent;

import io.starter.entity.SkillEntity;
import io.starter.model.Lines;
import io.starter.model.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

  private final SkillsDAO skillsDAO;

  @Autowired
  public DatabaseService(SkillsDAO skillsDAO) {
    this.skillsDAO = skillsDAO;
  }

  public void loadSkills(Lines<Skill> lines) {
    if (!lines.getLines().isEmpty()) {
      skillsDAO.saveAll(lines);
    }
  }

  public void updateSkills(Lines<Skill> lines) {
    List<SkillEntity> entitiesOnUpdate = skillsDAO.findAll();
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

  public void addNewSkills() {

  }
}
