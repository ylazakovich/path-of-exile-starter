package io.automation.telegram.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

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
