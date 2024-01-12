package io.automation.telegram.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Table(name = "event_cash")
//serves to save unhandled events after rebooting heroku
public class EventCashEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "serial")
  public Long id;
  @NotNull
  @Column(name = "time")
  public Date date;
  @NotNull
  public String description;
  @NotNull
  public long userId;

  public static EventCashEntity eventTo(Date date,
                                        String description,
                                        long userId) {
    EventCashEntity eventCashEntity = new EventCashEntity();
    eventCashEntity.date = date;
    eventCashEntity.description = description;
    eventCashEntity.userId = userId;
    return eventCashEntity;
  }
}
