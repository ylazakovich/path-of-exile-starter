package io.starter.dto;

import io.starter.entity.RateEntity;

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

  private Long leagueId;
  private String name;
  private double chaosEquivalentPrice;
  private double chaosEquivalentProfit;

  public AnalyzedSkillDto setChaosEquivalentPrice(RateEntity rateEntity) {
    this.chaosEquivalentPrice = rateEntity.getChaosEquivalent();
    return this;
  }
}
