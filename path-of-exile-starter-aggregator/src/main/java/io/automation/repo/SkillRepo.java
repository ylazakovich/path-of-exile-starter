package io.automation.repo;

import java.util.Optional;

import io.automation.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SkillRepo extends JpaRepository<SkillEntity, Long> {

  Optional<SkillEntity> findSkillById(Long id);

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE skills", nativeQuery = true)
  void truncateTable();
}
