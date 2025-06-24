package io.starter.type;

import java.util.function.Supplier;

import io.starter.model.ninja.Lines;

import org.springframework.core.ParameterizedTypeReference;

@FunctionalInterface
public interface TypeRefFactory<T> extends Supplier<ParameterizedTypeReference<Lines<T>>> {
}
