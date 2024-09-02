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
public class Currency {

  @JsonProperty("currencyTypeName")
  private String name;
  @JsonProperty("chaosEquivalent")
  private double chaosEquivalent;
}
