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

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
public class UserEntity extends Identity {

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "league_id")
  public @NonNull LeagueEntity leagueId;
  public @NonNull Long userId;
  public @NonNull String firstName;
  public String userName;
  public String lastName;
  public Integer lastMessageId;
  public @NonNull Integer skillPage;
}
