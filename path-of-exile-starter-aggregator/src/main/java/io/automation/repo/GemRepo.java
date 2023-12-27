package io.automation.repo;

import java.util.Optional;

import io.automation.entity.GemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GemRepo extends JpaRepository<GemEntity, Long> {

  void deleteGemById(Long id);

  Optional<GemEntity> findGemById(Long id);
}
