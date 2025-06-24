package io.starter.mapper;

import java.util.List;
import java.util.function.Function;

import io.starter.entity.SkillEntity;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;

import org.springframework.stereotype.Service;

@Service
public class SkillEntityMapper implements Function<Lines<Skill>, List<SkillEntity>> {

  @Override
  public List<SkillEntity> apply(Lines<Skill> data) {
    return data.getLines().stream()
        .map(skill -> new SkillEntity(
            skill.getName(),
            skill.getVariant(),
            skill.isCorrupted(),
            skill.getGemLevel(),
            skill.getGemQuality(),
            skill.getChaosEquivalent(),
            0.0))
        .toList();
  }

  public boolean matches(Skill skill, SkillEntity entity) {
    return skill.getName().equals(entity.getName())
        && skill.getGemLevel() == entity.getGemLevel()
        && skill.getGemQuality() == entity.getGemQuality()
        && skill.getVariant().equals(entity.getVariant())
        && skill.isCorrupted() == entity.getCorrupted();
  }
}
