package io.automation.telegram.dao;

import java.util.List;

import io.automation.telegram.entity.EventCashEntity;
import io.automation.telegram.repo.EventCashRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//handles events not dispatched after reboot heroku
public class EventCashDAO {

  private EventCashRepository eventCashRepository;

  @Autowired
  public void setEventCashRepository(EventCashRepository eventCashRepository) {
    this.eventCashRepository = eventCashRepository;
  }

  public List<EventCashEntity> findAll() {
    return eventCashRepository.findAll();
  }

  public void save(EventCashEntity eventCashEntity) {
    eventCashRepository.save(eventCashEntity);
  }

  public void delete(long id) {
    eventCashRepository.deleteById(id);
  }
}
