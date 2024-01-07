package io.automation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class SkillGem {

  @JsonProperty("name")
  private String name;
  @JsonProperty("variant")
  private String variant;
  @JsonProperty("corrupted")
  private boolean corrupted;
  @JsonProperty("gemLevel")
  private int gemLevel;
  @JsonProperty("gemQuality")
  private int gemQuality;
  @JsonProperty("chaosValue")
  private double chaosValue;
}
