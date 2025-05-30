package io.starter.repo;

import java.util.List;

import io.starter.entity.LeagueEntity;
import io.starter.entity.ProcessedSkillEntity;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedSkillsRepository extends JpaRepository<ProcessedSkillEntity, Long> {

  List<ProcessedSkillEntity> findAllByLeague(LeagueEntity league);

  List<ProcessedSkillEntity> findAllByLeague(LeagueEntity league, Sort sort);
}
