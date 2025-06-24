package io.starter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Table(name = "unique_jewels")
public class UniqueJewelEntity extends Identity {

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "league_id")
  private @NonNull LeagueEntity leagueId;
  private @NonNull String name;
  private @NonNull Double chaosEquivalent;
}
