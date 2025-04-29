package io.starter.repo;

import java.util.List;

import io.starter.entity.LeagueEntity;
import io.starter.entity.SkillEntity;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<SkillEntity, Long> {

  List<SkillEntity> findAllByLeagueId(LeagueEntity league);

  List<SkillEntity> findAllByLeagueId(LeagueEntity league, Sort sort);
}
