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

  public static Lines<UniqueJewel> generateLines() {
    return new Lines<>(Collections.singletonList(generate()));
  }

  public static UniqueJewel generate() {
    UniqueJewel uniqueJewel = new UniqueJewel();
    uniqueJewel.setName(faker.harryPotter().book());
    uniqueJewel.setChaosEquivalent(faker.number().randomDouble(1, 1, 5));
    return uniqueJewel;
  }
}
