package io.automation.telegram.entity;

import java.util.Date;

import io.automation.telegram.model.EventFreq;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "user_events")
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "event_id", columnDefinition = "serial")
  public int eventId;

  @Column(name = "time")
  @NotNull(message = "Need date!")
  public Date date;

  @Column(name = "description")
  @Size(min = 4, max = 200, message = "Description must be between 0 and 200 chars!")
  public String description;

  @Column(name = "event_freq", columnDefinition = "TIME")
  @Enumerated(EnumType.STRING)
  public EventFreq freq;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  public User user;

  public Event() {
  }

  public Event(int eventId,
               @NotNull(message = "Need date!") Date date,
               @Size(min = 4, max = 200, message = "Description must be between 0 and 200 chars!")
               String description,
               EventFreq freq, User user) {
    this.eventId = eventId;
    this.date = date;
    this.description = description;
    this.freq = freq;
    this.user = user;
  }
}
