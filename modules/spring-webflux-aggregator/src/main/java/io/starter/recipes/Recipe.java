package io.starter.recipes;

import java.util.List;

import io.starter.entity.LeagueEntity;

public interface Recipe<I, O> {
  boolean matches(List<I> ingredients);

  O craft(List<I> ingredients);

  double profit(List<I> ingredients);

  LeagueEntity getLeagueEntity();

  String getName();
}
