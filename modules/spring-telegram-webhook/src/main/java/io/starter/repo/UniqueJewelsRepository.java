package io.starter.repo;

import java.util.Optional;

import io.starter.entity.LeagueEntity;
import io.starter.entity.UniqueJewelEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UniqueJewelsRepository extends JpaRepository<UniqueJewelEntity, Long> {

  Optional<UniqueJewelEntity> findByNameAndLeague(String name, LeagueEntity league);
}
