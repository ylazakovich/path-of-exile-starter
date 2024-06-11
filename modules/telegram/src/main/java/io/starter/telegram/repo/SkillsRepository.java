package io.starter.telegram.repo;

import io.starter.telegram.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillsRepository extends JpaRepository<SkillEntity, Long> {

}
