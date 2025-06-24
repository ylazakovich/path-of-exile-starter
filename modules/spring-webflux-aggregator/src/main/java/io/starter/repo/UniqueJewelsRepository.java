package io.starter.repo;

import java.util.List;

import io.starter.entity.UniqueJewelEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UniqueJewelsRepository extends JpaRepository<UniqueJewelEntity, Long> {

  List<UniqueJewelEntity> findAllByLeagueId(Long league_id);
}
