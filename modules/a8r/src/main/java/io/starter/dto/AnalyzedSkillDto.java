package io.starter.dto;

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
public class AnalyzedSkillDto {

  private Long league_id;
  private String name;
  private double chaosEquivalentPrice;
  private double chaosEquivalentProfit;
}
