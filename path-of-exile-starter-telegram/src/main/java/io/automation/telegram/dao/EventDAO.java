package io.automation.telegram.dao;

import java.util.List;

import io.automation.telegram.entity.Event;
import io.automation.telegram.entity.User;
import io.automation.telegram.repo.EventRepository;
import io.automation.telegram.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventDAO {

  private final UserRepository userRepository;
  private final EventRepository eventRepository;

  @Autowired
  public EventDAO(UserRepository userRepository,
                  EventRepository eventRepository) {
    this.userRepository = userRepository;
    this.eventRepository = eventRepository;
  }

  public List<Event> findByUserId(long userId) {
    User user = userRepository.findById(userId);
    return user.events;
  }

  public List<Event> findAllEvent() {
    return eventRepository.findAll();
  }

  public Event findByEventId(long eventId) {
    return eventRepository.findByEventId(eventId);
  }

  public void remove(Event event) {
    eventRepository.delete(event);
  }

  public void save(Event event) {
    eventRepository.save(event);
  }
}
