package io.starter.mapper;

import java.util.List;
import java.util.function.Function;

import io.starter.entity.SkillEntity;
import io.starter.model.Lines;
import io.starter.model.Skill;

import org.springframework.stereotype.Service;

@Service
public class SkillEntityMapper implements Function<Lines<Skill>, List<SkillEntity>> {

  @Override
  public List<SkillEntity> apply(Lines<Skill> data) {
    return data.getLines().stream()
        .map(skill -> new SkillEntity(
            1L, // TODO: Need implement dynamically read league
            skill.getName(),
            skill.getVariant(),
            skill.isCorrupted(),
            skill.getGemLevel(),
            skill.getGemQuality(),
            skill.getChaosEquivalentPrice()))
        .toList();
  }
}
