package io.starter.recipes;

import java.util.List;

public interface Recipe<I, O> {
  boolean matches(List<I> ingredients);

  O craft(List<I> ingredients);

  List<I> required();
}
