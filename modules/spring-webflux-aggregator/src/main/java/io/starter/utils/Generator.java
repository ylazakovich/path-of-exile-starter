package io.starter.utils;

import java.util.List;

import io.starter.dto.AnalyzedSkillDto;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;

import lombok.experimental.UtilityClass;
import net.datafaker.Faker;

@UtilityClass
public class Generator {

  private final Faker faker = new Faker();

  public static Lines<Skill> generateLineWithSkills() {
    return new Lines<>(List.of(generateSkill()));
  }

  public static Skill generateSkill() {
    return Skill.builder()
        .name(faker.harryPotter().spell())
        .variant(faker.tire().aspectRatio())
        .chaosEquivalent(faker.number().randomDouble(5, 0, 1000))
        .corrupted(faker.random().nextBoolean())
        .gemLevel(faker.number().numberBetween(0, 20))
        .gemQuality(faker.number().numberBetween(0, 20))
        .build();
  }

  public static AnalyzedSkillDto generateAnalyzedSkill() {
    return AnalyzedSkillDto.builder()
        .name(faker.harryPotter().spell())
        .chaosEquivalentPrice(faker.number().randomDouble(1, 1, 5))
        .chaosEquivalentProfit(faker.number().randomDouble(5, 10, 1000))
        .build();
  }
}
