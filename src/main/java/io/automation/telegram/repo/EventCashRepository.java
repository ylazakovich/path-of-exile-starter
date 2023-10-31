package io.automation.telegram.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import io.automation.telegram.entity.EventCashEntity;

public interface EventCashRepository extends JpaRepository<EventCashEntity, Long> {

  EventCashEntity findById(long id);
}
