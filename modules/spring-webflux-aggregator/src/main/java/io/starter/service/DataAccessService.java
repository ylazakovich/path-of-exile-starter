package io.starter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.starter.dto.AnalyzedSkillDto;
import io.starter.entity.LeagueEntity;
import io.starter.entity.ProcessedSkillEntity;
import io.starter.entity.RateEntity;
import io.starter.entity.SkillEntity;
import io.starter.entity.UniqueJewelEntity;
import io.starter.entity.VendorRecipeEntity;
import io.starter.mapper.LeagueEntityMapper;
import io.starter.model.path_of_exile.League;
import io.starter.repo.LeaguesRepository;
import io.starter.repo.ProcessedSkillsRepository;
import io.starter.repo.RatesRepository;
import io.starter.repo.SkillsRepository;
import io.starter.repo.UniqueJewelsRepository;
import io.starter.repo.VendorRecipeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DataAccessService {

  private final ProcessedSkillsRepository processedSkillsRepository;
  private final SkillsRepository skillsRepository;
  private final RatesRepository ratesRepository;
  private final UniqueJewelsRepository uniqueJewelsRepository;
  private final LeaguesRepository leaguesRepository;
  private final VendorRecipeRepository vendorRecipeRepository;

  @Transactional(readOnly = true)
  public List<SkillEntity> findSkillsByLeague(LeagueEntity league) {
    return skillsRepository.findAllByLeague(league);
  }

  @Transactional(readOnly = true)
  public SkillEntity findSkillBylLeagueAndNameAndVariant(LeagueEntity league, String name, String variant) {
    return skillsRepository.findByLeagueAndNameAndVariant(league, name, variant);
  }

  @Transactional(readOnly = true)
  public List<ProcessedSkillEntity> findProcessedSkillsByLeague(LeagueEntity league) {
    return processedSkillsRepository.findAllByLeague(league);
  }

  @Transactional(readOnly = true)
  public Optional<RateEntity> findRateByNameAndLeague(String name, LeagueEntity league) {
    return Optional.ofNullable(ratesRepository.findByNameAndLeague(name, league));
  }

  @Transactional(readOnly = true)
  public List<RateEntity> findRatesByLeague(LeagueEntity league) {
    return ratesRepository.findAllByLeague(league);
  }

  @Transactional(readOnly = true)
  public List<UniqueJewelEntity> findUniqueJewelsByLeague(LeagueEntity league) {
    return uniqueJewelsRepository.findAllByLeague(league);
  }

  @Transactional(readOnly = true)
  public UniqueJewelEntity findUniqueJewelByLeague(String name, LeagueEntity league) {
    return uniqueJewelsRepository.findByNameAndLeague(name, league);
  }

  @Transactional(readOnly = true)
  public Optional<LeagueEntity> findLeagueById(Long id) {
    return leaguesRepository.findById(id);
  }

  @Transactional(readOnly = true)
  public Optional<VendorRecipeEntity> findVendorRecipeByNameAndLeague(String value, LeagueEntity league) {
    return vendorRecipeRepository.findByLeagueAndName(league, value);
  }

  @Transactional(readOnly = true)
  public LeagueEntity findLeagueByName(String name) {
    return leaguesRepository.findByName(name);
  }

  @Transactional(readOnly = true)
  public List<LeagueEntity> findLeagues() {
    return leaguesRepository.findAll();
  }

  @Transactional
  public void saveUniqueJewels(List<UniqueJewelEntity> uniqueJewelEntityList) {
    uniqueJewelsRepository.saveAll(uniqueJewelEntityList);
  }

  @Transactional
  public void saveSkills(List<SkillEntity> skillEntityList) {
    skillsRepository.saveAll(skillEntityList);
  }

  @Transactional
  public void saveLeagues(List<League> data) {
    LeagueEntityMapper mapper = new LeagueEntityMapper(this);
    List<LeagueEntity> entityList = mapper.apply(data);
    leaguesRepository.saveAll(entityList);
  }

  @Transactional
  public void saveRates(List<RateEntity> rateEntityList) {
    ratesRepository.saveAll(rateEntityList);
  }

  @Transactional
  public void saveVendorRecipe(VendorRecipeEntity entity) {
    vendorRecipeRepository.save(entity);
  }

  @Transactional
  public void addProcessedSkills(LeagueEntity league, List<AnalyzedSkillDto> analyzedSkills) {
    if (findProcessedSkillsByLeague(league).isEmpty()) {
      final List<ProcessedSkillEntity> entities = analyzedSkills.stream()
          .map(skill -> {
            ProcessedSkillEntity skillEntity = new ProcessedSkillEntity();
            skillEntity.setLeague(league);
            skillEntity.setSkill(findSkillBylLeagueAndNameAndVariant(league, skill.getName(), "1/20"));
            skillEntity.setChaosEquivalentPrice(skill.getChaosEquivalentPrice());
            skillEntity.setChaosEquivalentProfit(skill.getChaosEquivalentProfit());
            return skillEntity;
          }).collect(Collectors.toList());
      processedSkillsRepository.saveAll(entities);
    }
  }

  @Transactional
  public void updateProcessedSkills(LeagueEntity league, List<AnalyzedSkillDto> analyzedSkills) {
    List<ProcessedSkillEntity> entitiesOnUpdate = processedSkillsRepository.findAllByLeague(league);
    analyzedSkills.forEach(skill ->
        entitiesOnUpdate.stream()
            .filter(entity ->
                entity.getSkill().equals(findSkillBylLeagueAndNameAndVariant(league, skill.getName(), "1/20")))
            .findFirst()
            .ifPresent(matchedEntity -> {
                  matchedEntity.setChaosEquivalentPrice(skill.getChaosEquivalentPrice());
                  matchedEntity.setChaosEquivalentProfit(skill.getChaosEquivalentProfit());
                }
            )
    );
    processedSkillsRepository.saveAll(entitiesOnUpdate);
  }

  @Transactional
  public void addNewProcessedSkill(LeagueEntity league, List<AnalyzedSkillDto> analyzedSkills) {
    List<SkillEntity> allEntities = findSkillsByLeague(league);
    List<ProcessedSkillEntity> entitiesOnAdding = new ArrayList<>();
    analyzedSkills.stream()
        .filter(skill -> allEntities.stream().noneMatch(entity -> entity.getName().equals(skill.getName())))
        .forEach(skill -> {
          ProcessedSkillEntity skillEntity = new ProcessedSkillEntity();
          skillEntity.setLeague(league);
          skillEntity.setSkill(findSkillBylLeagueAndNameAndVariant(league, skill.getName(), "1/20"));
          skillEntity.setChaosEquivalentPrice(skill.getChaosEquivalentPrice());
          skillEntity.setChaosEquivalentProfit(skill.getChaosEquivalentProfit());
          entitiesOnAdding.add(skillEntity);
        });
    processedSkillsRepository.saveAll(entitiesOnAdding);
  }
}
