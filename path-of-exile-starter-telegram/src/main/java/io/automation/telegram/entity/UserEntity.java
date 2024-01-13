package io.automation.telegram.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "users")
public class UserEntity {

  @Id
  @Column(name = "id")
  public long id;
  public String name;
  public int timeZone;
  @Column(name = "on_off")
  public boolean on;
}
