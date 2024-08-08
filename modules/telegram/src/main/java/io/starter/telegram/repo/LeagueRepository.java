package io.starter.telegram.repo;

import io.starter.telegram.entity.LeagueEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<LeagueEntity, Long> {

}
