package io.automation.dto;

import java.util.List;
import java.util.stream.Collectors;

import io.automation.entity.SkillEntity;
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
public class SkillDTO {

  private String name;
  private String variant;
  private boolean corrupted;
  private int gemLevel;
  private int gemQuality;
  private double chaosValue;

  public static List<SkillDTO> convertToList(List<SkillEntity> entities) {
    return entities.stream()
        .map(entity -> new SkillDTO(
            entity.getName(),
            entity.getVariant(),
            entity.isCorrupted(),
            entity.getGemLevel(),
            entity.getGemQuality(),
            entity.getChaosValue()
        ))
        .collect(Collectors.toList());
  }

  public static List<SkillEntity> convertToEntity(List<SkillDTO> data) {
    return data.stream()
        .map(skillDTO -> new SkillEntity(
            skillDTO.getName(),
            skillDTO.getVariant(),
            skillDTO.isCorrupted(),
            skillDTO.getGemLevel(),
            skillDTO.getGemQuality(),
            skillDTO.getChaosValue()))
        .collect(Collectors.toList());
  }
}
