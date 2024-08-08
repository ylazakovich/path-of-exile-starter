package io.starter.repo;

import io.starter.entity.LeagueEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaguesRepository extends JpaRepository<LeagueEntity, Long> {

  LeagueEntity findByName(String name);

}
