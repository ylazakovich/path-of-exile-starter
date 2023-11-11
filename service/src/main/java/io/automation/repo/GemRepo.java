package io.automation.repo;

import java.util.Optional;

import io.automation.entity.Gem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GemRepo extends JpaRepository<Gem, Long> {

  void deleteGemById(Long id);

  Optional<Gem> findGemById(Long id);
}
