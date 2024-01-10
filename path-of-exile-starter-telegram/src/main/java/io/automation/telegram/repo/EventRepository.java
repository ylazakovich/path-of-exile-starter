package io.automation.telegram.repo;

import io.automation.telegram.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

  Event findByEventId(long id);
}
