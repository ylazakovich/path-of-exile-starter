package io.automation.utils;

import java.util.List;

import io.automation.model.Lines;
import io.automation.model.SkillGem;
import lombok.experimental.UtilityClass;
import net.datafaker.Faker;

@UtilityClass
public class Generator {

  private final Faker faker = new Faker();

  public static Lines<SkillGem> generateLineWithSkills() {
    return new Lines<>(List.of(generateSkill()));
  }

  public static SkillGem generateSkill() {
    return SkillGem.builder()
        .name(faker.harryPotter().spell())
        .variant(faker.tire().aspectRatio())
        .chaosValue(faker.number().randomDouble(5, 0, 1000))
        .corrupted(faker.random().nextBoolean())
        .gemLevel(faker.number().numberBetween(0, 20))
        .gemQuality(faker.number().numberBetween(0, 20))
        .build();
  }
}
