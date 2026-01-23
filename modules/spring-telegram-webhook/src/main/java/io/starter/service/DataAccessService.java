package io.starter.service;

import java.util.List;
import java.util.Optional;

import io.starter.entity.LeagueEntity;
import io.starter.entity.ProcessedSkillEntity;
import io.starter.entity.UniqueJewelEntity;
import io.starter.entity.VendorRecipeEntity;
import io.starter.model.aggregator.Skill;
import io.starter.repo.ProcessedSkillsRepository;
import io.starter.repo.UniqueJewelsRepository;
import io.starter.repo.VendorRecipeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DataAccessService {

  private final ProcessedSkillsRepository processedSkillsRepository;
  private final VendorRecipeRepository vendorRecipeRepository;
  private final UniqueJewelsRepository uniqueJewelsRepository;

  @Autowired
  public DataAccessService(ProcessedSkillsRepository processedSkillsRepository,
                           VendorRecipeRepository vendorRecipeRepository,
                           UniqueJewelsRepository uniqueJewelsRepository) {
    this.processedSkillsRepository = processedSkillsRepository;
    this.vendorRecipeRepository = vendorRecipeRepository;
    this.uniqueJewelsRepository = uniqueJewelsRepository;
  }

  public List<Skill> findAllSkills(LeagueEntity league) {
    final String column = "chaosEquivalentProfit";
    List<ProcessedSkillEntity> all = processedSkillsRepository.searchByNameContaining(
        league,
        "Support",
        0.0,
        Sort.by(Sort.Direction.DESC, column)
    );
    return all.stream().map(entity -> {
      Skill skill = new Skill();
      skill.setName(entity.getSkill().getName());
      skill.setChaosEquivalentPrice(entity.getChaosEquivalentPrice());
      skill.setChaosEquivalentProfit(entity.getChaosEquivalentProfit());
      return skill;
    }).toList();
  }

  public Optional<VendorRecipeEntity> findVendorRecipeByNameAndLeague(String name, LeagueEntity league) {
    return vendorRecipeRepository.findByLeagueAndName(league, name);
  }

  public Optional<UniqueJewelEntity> findUniqueJewelByNameAndLeague(String name, LeagueEntity league) {
    return uniqueJewelsRepository.findByNameAndLeague(name, league);
  }
}
