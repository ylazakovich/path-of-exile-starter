package io.starter.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "skills")
public class SkillEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "league_id")
  private @NonNull LeagueEntity league;
  private @NonNull String name;
  private @NonNull String variant;
  private @NonNull Boolean corrupted;
  private @NonNull Integer gemLevel;
  private @NonNull Integer gemQuality;
  private @NonNull Double chaosEquivalent;
  private @NonNull Double divineEquivalent;
}
