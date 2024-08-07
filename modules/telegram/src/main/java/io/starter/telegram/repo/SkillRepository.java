package io.starter.telegram.repo;

import java.util.List;

import io.starter.telegram.entity.LeagueEntity;
import io.starter.telegram.entity.SkillEntity;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<SkillEntity, Long> {

  List<SkillEntity> findAllByLeagueId(LeagueEntity league);
  List<SkillEntity> findAllByLeagueId(LeagueEntity league, Sort sort);
}
