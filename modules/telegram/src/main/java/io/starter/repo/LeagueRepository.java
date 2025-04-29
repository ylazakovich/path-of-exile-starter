package io.starter.repo;

import io.starter.entity.LeagueEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<LeagueEntity, Long> {

  LeagueEntity findById(long id);
}
