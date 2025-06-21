package io.starter.repo;

import java.util.List;

import io.starter.entity.LeagueEntity;
import io.starter.entity.ProcessedSkillEntity;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProcessedSkillsRepository extends JpaRepository<ProcessedSkillEntity, Long> {

  List<ProcessedSkillEntity> findAllByLeague(LeagueEntity league);

  List<ProcessedSkillEntity> findAllByLeague(LeagueEntity league, Sort sort);

  List<ProcessedSkillEntity> findAllByLeagueAndChaosEquivalentProfitGreaterThan(LeagueEntity league,
                                                                                double chaosEquivalentProfit,
                                                                                Sort sort);

  @Query("""
        SELECT p FROM ProcessedSkillEntity p
        WHERE p.league = :league
          AND LOWER(p.skill.name) LIKE LOWER(CONCAT('%', :namePart, '%'))
          AND p.chaosEquivalentProfit > :chaosProfit
      """)
  List<ProcessedSkillEntity> searchByNameContaining(
      @Param("league") LeagueEntity league,
      @Param("namePart") String namePart,
      @Param("chaosProfit") double chaosEquivalentProfit,
      Sort sort
  );
}
