package io.automation.repo;

import java.util.Optional;

import io.automation.entity.SkillGemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface GemRepo extends JpaRepository<SkillGemEntity, Long> {

  void deleteGemById(Long id);

  Optional<SkillGemEntity> findGemById(Long id);

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE gems", nativeQuery = true)
  void truncateTable();
}
