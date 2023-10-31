package io.automation.telegram.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "event_cash")
//serves to save unhandled events after rebooting heroku
public class EventCashEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "serial")
  public long id;

  @Column(name = "time")
  public Date date;

  @Column(name = "description")
  public String description;

  @Column(name = "user_id")
  public long userId;

  public EventCashEntity() {
  }

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
