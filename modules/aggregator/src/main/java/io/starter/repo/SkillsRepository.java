package io.starter.repo;

import io.starter.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillsRepository extends JpaRepository<SkillEntity, Long> {

}
