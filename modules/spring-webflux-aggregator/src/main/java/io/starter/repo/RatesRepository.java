package io.starter.repo;

import java.util.List;

import io.starter.entity.RateEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RatesRepository extends JpaRepository<RateEntity, Long> {

  List<RateEntity> findAllByLeagueId(Long league_id);

  RateEntity findByNameAndLeagueId(String name, Long league_id);
}
