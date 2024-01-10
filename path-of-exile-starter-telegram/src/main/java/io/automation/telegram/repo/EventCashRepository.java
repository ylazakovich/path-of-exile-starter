package io.automation.telegram.repo;

import io.automation.telegram.entity.EventCashEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventCashRepository extends JpaRepository<EventCashEntity, Long> {

  EventCashEntity findById(long id);
}
