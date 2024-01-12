package io.automation.telegram.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "users")
public class User {

  @Id
  @Column(name = "id")
  public long id;
  public String name;
  public int timeZone;
  @OneToMany(mappedBy = "user")
  public List<Event> events;
  @Column(name = "on_off")
  public boolean on;
}
