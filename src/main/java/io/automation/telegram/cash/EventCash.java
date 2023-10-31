package io.automation.telegram.cash;

import org.springframework.stereotype.Service;
import io.automation.telegram.entity.Event;

import java.util.HashMap;
import java.util.Map;

@Service
// used to save entered event data per session
public class EventCash {

  private final Map<Long, Event> eventMap = new HashMap<>();

  public Map<Long, Event> getEventMap() {
    return eventMap;
  }

  public void saveEventCash(long userId, Event event) {
    eventMap.put(userId, event);
  }
}
