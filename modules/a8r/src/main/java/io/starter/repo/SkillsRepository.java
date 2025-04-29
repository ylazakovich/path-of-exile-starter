package io.starter.repo;

import java.util.List;

import io.starter.entity.LeagueEntity;
import io.starter.entity.SkillEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillsRepository extends JpaRepository<SkillEntity, Long> {

  List<SkillEntity> findAllByLeague(LeagueEntity league);

  SkillEntity findByLeagueAndName(LeagueEntity league, String name);
}
