package io.starter.model.ninja;

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
public class UniqueJewel {

  @JsonProperty("name")
  private String name;
  @JsonProperty("chaosValue")
  private double chaosEquivalent;
}
