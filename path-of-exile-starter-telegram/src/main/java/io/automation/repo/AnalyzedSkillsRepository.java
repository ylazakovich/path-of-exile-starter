package io.automation.repo;

import io.automation.entity.AnalyzedSkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AnalyzedSkillsRepository extends JpaRepository<AnalyzedSkillEntity, Long> {

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE analyzed_skills", nativeQuery = true)
  void truncateTable();
}
