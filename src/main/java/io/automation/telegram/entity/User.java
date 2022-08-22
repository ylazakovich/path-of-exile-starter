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
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "time_zone", columnDefinition = "default 0")
    //sets the broadcast time of events for your time zone
    private int timeZone;

    @OneToMany(mappedBy="user")
    private List<Event> events;

    @Column(name = "on_off")
    // on/off send event
    private boolean on;

    public User() {
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public User setTimeZone(int timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public List<Event> getEvents() {
        return events;
    }

    public User setEvents(List<Event> events) {
        this.events = events;
        return this;
    }

    public boolean isOn() {
        return on;
    }

    public User setOn(boolean on) {
        this.on = on;
        return this;
    }
}
