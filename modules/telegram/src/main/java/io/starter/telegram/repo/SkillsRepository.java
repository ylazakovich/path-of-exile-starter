package io.starter.telegram.repo;

import java.util.List;

import io.starter.telegram.entity.SkillEntity;

import lombok.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillsRepository extends JpaRepository<SkillEntity, Long> {

  @NonNull List<SkillEntity> findAll(@NonNull Sort sort);

}
