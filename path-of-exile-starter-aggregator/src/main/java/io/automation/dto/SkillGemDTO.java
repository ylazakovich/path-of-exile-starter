package io.automation.dto;

import java.util.List;
import java.util.stream.Collectors;

import io.automation.entity.SkillGemEntity;
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
public class SkillGemDTO {

  private String name;
  private String variant;
  private boolean corrupted;
  private int gemLevel;
  private int gemQuality;
  private double chaosValue;

  public static List<SkillGemDTO> convertToList(List<SkillGemEntity> entities) {
    return entities.stream()
        .map(entity -> new SkillGemDTO(
            entity.getName(),
            entity.getVariant(),
            entity.isCorrupted(),
            entity.getGemLevel(),
            entity.getGemQuality(),
            entity.getChaosValue()
        ))
        .collect(Collectors.toList());
  }

  public static List<SkillGemEntity> convertToEntity(List<SkillGemDTO> data) {
    return data.stream()
        .map(skillGemDTO -> new SkillGemEntity(
            skillGemDTO.getName(),
            skillGemDTO.getVariant(),
            skillGemDTO.isCorrupted(),
            skillGemDTO.getGemLevel(),
            skillGemDTO.getGemQuality(),
            skillGemDTO.getChaosValue()))
        .collect(Collectors.toList());
  }
}
