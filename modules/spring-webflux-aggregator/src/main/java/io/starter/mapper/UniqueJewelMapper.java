package io.starter.mapper;

import java.util.List;
import java.util.function.Function;

import io.starter.entity.UniqueJewelEntity;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.UniqueJewel;

import org.springframework.stereotype.Service;

@Service
public class UniqueJewelMapper implements Function<Lines<UniqueJewel>, List<UniqueJewelEntity>> {

  @Override
  public List<UniqueJewelEntity> apply(Lines<UniqueJewel> data) {
    return data.getLines().stream()
        .map(skill -> new UniqueJewelEntity(skill.getName(), skill.getChaosEquivalent()))
        .toList();
  }

  public boolean matches(UniqueJewel jewel, UniqueJewelEntity entity) {
    return jewel.getName().equals(entity.getName());
  }
}
