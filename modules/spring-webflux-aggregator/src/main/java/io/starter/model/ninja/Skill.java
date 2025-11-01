package io.starter.model.ninja;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Skill {

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
  private double chaosEquivalent;
}
