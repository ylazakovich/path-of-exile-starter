package io.starter.telegram.repo;

import java.util.List;

import io.starter.telegram.entity.AnalyzedSkillEntity;
import lombok.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AnalyzedSkillsRepository extends JpaRepository<AnalyzedSkillEntity, Long> {

  @NonNull List<AnalyzedSkillEntity> findAll(@NonNull Sort sort);

  @Transactional
  @Modifying
  @Query(value = "TRUNCATE TABLE analyzed_skills", nativeQuery = true)
  void truncateTable();
}
