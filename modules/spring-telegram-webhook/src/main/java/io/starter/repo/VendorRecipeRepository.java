package io.starter.repo;

import java.util.Optional;

import io.starter.entity.LeagueEntity;
import io.starter.entity.VendorRecipeEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRecipeRepository extends JpaRepository<VendorRecipeEntity, Long> {

  Optional<VendorRecipeEntity> findByLeagueAndName(LeagueEntity league, String name);
}
