package io.starter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "users")
public class UserEntity extends WithJoinLeague {

  public @NotNull Long userId;
  public @NotNull String firstName;
  public String userName;
  public String lastName;
  public Integer lastMessageId;
  public @NotNull Integer skillPage;
}
