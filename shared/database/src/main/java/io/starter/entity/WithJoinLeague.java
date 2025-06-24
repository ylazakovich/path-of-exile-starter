package io.starter.entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public class WithJoinLeague extends Identity {

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "league_id", nullable = false)
  private LeagueEntity league;
}
