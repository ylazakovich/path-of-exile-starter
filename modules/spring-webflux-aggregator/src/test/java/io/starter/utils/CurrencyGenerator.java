package io.starter.utils;

import java.util.Collections;

import io.starter.model.ninja.Currency;
import io.starter.model.ninja.Lines;

import net.datafaker.Faker;

public final class CurrencyGenerator {

  private static final Faker faker = new Faker();

  private CurrencyGenerator() {
    // utility class
  }

  public static Lines<Currency> generateLines() {
    return new Lines<>(Collections.singletonList(generate()));
  }

  public static Currency generate() {
    Currency currency = new Currency();
    currency.setName(faker.money().currency());
    currency.setChaosEquivalent(faker.number().randomDouble(1, 1, 5));
    return null;
  }
}
