package io.automation.telegram.DAO;

import io.automation.telegram.entity.EventCashEntity;
import io.automation.telegram.repo.EventCashRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//handles events not dispatched after reboot heroku
public class EventCashDAO {

    private EventCashRepository eventCashRepository;

    @Autowired
    public void setEventCashRepository(EventCashRepository eventCashRepository) {
        this.eventCashRepository = eventCashRepository;
    }

    public List<EventCashEntity> findAllEventCash() {
        return eventCashRepository.findAll();
    }

    public void save(EventCashEntity eventCashEntity) {
        eventCashRepository.save(eventCashEntity);
    }

    public void delete(long id) {
        eventCashRepository.deleteById(id);
    }
}
