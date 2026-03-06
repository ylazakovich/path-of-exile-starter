package io.starter.repo;

import java.util.Optional;
import java.util.List;

import io.starter.entity.LeagueEntity;
import io.starter.entity.VendorRecipeEntity;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRecipeRepository extends JpaRepository<VendorRecipeEntity, Long> {

  List<VendorRecipeEntity> findAllByLeague(LeagueEntity league, Sort sort);

  Optional<VendorRecipeEntity> findByLeagueAndName(LeagueEntity league, String name);
}
