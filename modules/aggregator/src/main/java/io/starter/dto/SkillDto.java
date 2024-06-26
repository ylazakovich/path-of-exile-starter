package io.starter.dto;

import java.util.List;
import java.util.stream.Collectors;

import io.starter.entity.SkillEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SkillDto {

  private String name;
  private String variant;
  private boolean corrupted;
  private int gemLevel;
  private int gemQuality;
  private double chaosEquivalentPrice;

  public static List<SkillDto> convertToList(List<SkillEntity> entities) {
    return entities.stream()
        .map(entity -> new SkillDto(
            entity.getName(),
            entity.getVariant(),
            entity.getCorrupted(),
            entity.getGemLevel(),
            entity.getGemQuality(),
            entity.getChaosEquivalentPrice()
        ))
        .collect(Collectors.toList());
  }
}
