package io.automation.telegram.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import io.automation.telegram.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

  Event findByEventId(long id);
}
