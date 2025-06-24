package io.starter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Table(name = "processed_skills")
public class ProcessedSkillEntity extends WithJoinLeague {

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "skill_id", nullable = false)
  private SkillEntity skill;

  @Column(name = "chaos_equivalent_price", nullable = false)
  private Double chaosEquivalentPrice;

  @Column(name = "chaos_equivalent_profit", nullable = false)
  private Double chaosEquivalentProfit;

}
