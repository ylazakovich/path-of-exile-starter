package io.starter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "processed_skills")
public class ProcessedSkillEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

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
