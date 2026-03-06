package io.starter.repo;

import java.util.Optional;

import io.starter.entity.LeagueEntity;
import io.starter.entity.RateEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<RateEntity, Long> {

  Optional<RateEntity> findByNameAndLeague(String name, LeagueEntity league);
}
