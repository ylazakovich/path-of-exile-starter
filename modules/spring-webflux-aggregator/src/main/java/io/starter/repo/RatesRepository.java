package io.starter.repo;

import java.util.List;

import io.starter.entity.LeagueEntity;
import io.starter.entity.RateEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RatesRepository extends JpaRepository<RateEntity, Long> {

  List<RateEntity> findAllByLeague(LeagueEntity league);

  RateEntity findByNameAndLeague(String name, LeagueEntity league);
}
