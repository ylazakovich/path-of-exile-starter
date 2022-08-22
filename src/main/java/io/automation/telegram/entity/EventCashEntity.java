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
    @Column( name = "id", columnDefinition = "serial")
    private long id;

    @Column(name = "time")
    private Date date;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id")
    private long userId;

    public EventCashEntity() {
    }

    public static EventCashEntity eventTo(Date date, String description, long userId) {
        EventCashEntity eventCashEntity = new EventCashEntity();
        eventCashEntity.setDate(date);
        eventCashEntity.setDescription(description);
        eventCashEntity.setUserId(userId);
        return eventCashEntity;
    }

    public long getId() {
        return id;
    }

    public EventCashEntity setId(long id) {
        this.id = id;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public EventCashEntity setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EventCashEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public EventCashEntity setUserId(long userId) {
        this.userId = userId;
        return this;
    }
}
