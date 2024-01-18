package io.starter.utils;

import java.util.List;

import io.starter.dto.AnalyzedSkillDTO;
import io.starter.model.Lines;
import io.starter.model.Skill;
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
        .chaosValue(faker.number().randomDouble(5, 0, 1000))
        .corrupted(faker.random().nextBoolean())
        .gemLevel(faker.number().numberBetween(0, 20))
        .gemQuality(faker.number().numberBetween(0, 20))
        .build();
  }

  public static AnalyzedSkillDTO generateAnalyzedSkill() {
    return AnalyzedSkillDTO.builder()
        .name(faker.harryPotter().spell())
        .craftCost(faker.number().randomDouble(1, 1, 5))
        .profit(faker.number().randomDouble(5, 10, 1000))
        .build();
  }
}
