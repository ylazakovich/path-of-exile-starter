package io.starter.utils;

import java.util.Collections;

import io.starter.dto.AnalyzedSkillDto;
import io.starter.entity.RateEntity;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;

import net.datafaker.Faker;

public final class SkillGenerator {

  private static final Faker faker = new Faker();

  private SkillGenerator() {
    // utility class
  }

  public static Lines<Skill> generateLines() {
    return new Lines<>(Collections.singletonList(generate()));
  }

  public static Skill generate() {
    Skill skill = new Skill();
    skill.setName(faker.harryPotter().spell());
    skill.setVariant(faker.tire().aspectRatio());
    skill.setChaosEquivalent(faker.number().randomDouble(5, 0, 1000));
    skill.setCorrupted(faker.random().nextBoolean());
    skill.setGemLevel(faker.number().numberBetween(0, 20));
    skill.setGemQuality(faker.number().numberBetween(0, 20));
    return skill;
  }

  public static AnalyzedSkillDto generateProcessedSkill() {
    AnalyzedSkillDto processedSkill = new AnalyzedSkillDto();
    RateEntity rateEntity = new RateEntity();
    rateEntity.setChaosEquivalent(faker.number().randomDouble(1, 1, 5));
    processedSkill.setName(faker.harryPotter().spell());
    processedSkill.setChaosEquivalentPrice(rateEntity);
    processedSkill.setChaosEquivalentProfit(faker.number().randomDouble(5, 10, 1000));
    return processedSkill;
  }
}
