package io.starter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "processed_skills")
@EqualsAndHashCode(callSuper = true)
public class ProcessedSkillEntity extends Identity {

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "league_id", nullable = false)
  private LeagueEntity league;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "skill_id", nullable = false)
  private SkillEntity skill;

  @Column(name = "chaos_equivalent_price", nullable = false)
  private Double chaosEquivalentPrice;

  @Column(name = "chaos_equivalent_profit", nullable = false)
  private Double chaosEquivalentProfit;

}
