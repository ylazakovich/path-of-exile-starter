package io.automation.telegram.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

  @Id
  @Column(name = "id")
  public long id;

  @Column(name = "name")
  public String name;

  @Column(name = "time_zone", columnDefinition = "default 0")
  //sets the broadcast time of events for your time zone
  public int timeZone;

  @OneToMany(mappedBy = "user")
  public List<Event> events;

  @Column(name = "on_off")
  // on/off send event
  public boolean on;

  public User() {
  }
}
