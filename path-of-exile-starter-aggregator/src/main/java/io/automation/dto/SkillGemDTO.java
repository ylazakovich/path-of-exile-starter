package io.automation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
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
}
