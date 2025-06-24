package io.starter.utils;

import java.util.Collections;

import io.starter.model.ninja.Lines;
import io.starter.model.ninja.UniqueJewel;

import net.datafaker.Faker;

public final class UniqueJewelGenerator {

  private static final Faker faker = new Faker();

  private UniqueJewelGenerator() {
    // utility class
  }

  public static Lines<UniqueJewel> generateLineWithSkills() {
    return new Lines<>(Collections.singletonList(generateUniqueJewel()));
  }

  public static UniqueJewel generateUniqueJewel() {
    UniqueJewel uniqueJewel = new UniqueJewel();
    uniqueJewel.setName(faker.harryPotter().book());
    uniqueJewel.setChaosEquivalent(faker.number().randomDouble(1, 1, 5));
    return uniqueJewel;
  }
}
