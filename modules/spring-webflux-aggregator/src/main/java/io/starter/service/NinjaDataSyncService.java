package io.starter.service;

import java.util.List;

import io.starter.dao.RatesDao;
import io.starter.dao.UniqueJewelsDao;
import io.starter.entity.LeagueEntity;
import io.starter.entity.RateEntity;
import io.starter.entity.SkillEntity;
import io.starter.mapper.SkillEntityMapper;
import io.starter.model.ninja.Currency;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;
import io.starter.model.ninja.UniqueJewel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NinjaDataSyncService {

  private final UniqueJewelsDao uniqueJewelsDao;
  private final RatesDao ratesDao;
  private final DataAccessService dataAccessService;
  private final RateService rateService;
  private final SkillEntityMapper skillEntityMapper;

  @Autowired
  public NinjaDataSyncService(UniqueJewelsDao uniqueJewelsDao,
                              RatesDao ratesDao,
                              DataAccessService dataAccessService,
                              RateService rateService,
                              SkillEntityMapper skillEntityMapper) {
    this.uniqueJewelsDao = uniqueJewelsDao;
    this.ratesDao = ratesDao;
    this.dataAccessService = dataAccessService;
    this.rateService = rateService;
    this.skillEntityMapper = skillEntityMapper;
  }

  private <T> List<T> safeLines(Lines<T> lines) {
    return (lines != null && lines.getLines() != null)
        ? lines.getLines()
        : List.of();
  }

  public void loadCurrency(Lines<Currency> lines, LeagueEntity league) {
    List<Currency> currencyLines = safeLines(lines);
    if (dataAccessService.findRatesByLeague(league).isEmpty() && !currencyLines.isEmpty()) {
      ratesDao.saveAll(lines, league.getId());
    }
  }

  public void loadUniqueJewels(Lines<UniqueJewel> lines, LeagueEntity league) {
    List<UniqueJewel> jewelLines = safeLines(lines);
    if (dataAccessService.findRatesByLeague(league).isEmpty() && !jewelLines.isEmpty()) {
      uniqueJewelsDao.saveAll(lines, league.getId());
    }
  }

  public void loadSkills(Lines<Skill> lines, LeagueEntity league) {
    List<Skill> skills = safeLines(lines);
    if (dataAccessService.findSkillsByLeague(league).isEmpty() && !skills.isEmpty()) {
      dataAccessService.findLeagueById(league.getId()).ifPresent(leagueEntity -> {
        List<SkillEntity> entityList = skillEntityMapper.apply(lines);
        entityList.forEach(skillEntity -> {
          skillEntity.setLeague(leagueEntity);
          skillEntity.setDivineEquivalent(rateService.toDivineEquivalent(skillEntity.getChaosEquivalent(), league));
        });
        dataAccessService.saveSkills(entityList);
      });
    }
  }

  public void updateCurrencies(Lines<Currency> lines, LeagueEntity league) {
    List<Currency> currencyLines = safeLines(lines);
    List<RateEntity> entitiesOnUpdate = dataAccessService.findRatesByLeague(league);
    entitiesOnUpdate.forEach(entity -> currencyLines.stream()
        .filter(currency -> currency.getName().equals(entity.getName()))
        .findFirst()
        .ifPresent(currency -> entity.setChaosEquivalent(currency.getChaosEquivalent()))
    );
    ratesDao.saveAll(entitiesOnUpdate, league.getId());
  }

  public void updateSkills(Lines<Skill> lines, LeagueEntity league) {
    List<Skill> skillLines = safeLines(lines);
    List<SkillEntity> entitiesOnUpdate = dataAccessService.findSkillsByLeague(league);
    entitiesOnUpdate.forEach(entity -> skillLines.stream()
        .filter(skill -> skill.getName().equals(entity.getName())
            && skill.getGemLevel() == entity.getGemLevel()
            && skill.getGemQuality() == entity.getGemQuality()
            && skill.isCorrupted() == entity.getCorrupted())
        .findFirst()
        .ifPresent(skill -> entity.setChaosEquivalent(skill.getChaosEquivalent()))
    );
    dataAccessService.findLeagueById(league.getId()).ifPresent(leagueEntity -> {
      entitiesOnUpdate.forEach(entity -> {
        entity.setLeague(leagueEntity);
        entity.setDivineEquivalent(rateService.toDivineEquivalent(entity.getChaosEquivalent(), league));
      });
      dataAccessService.saveSkills(entitiesOnUpdate);
    });
  }

  public void addNew(Lines<Skill> lines, LeagueEntity league) {
    List<SkillEntity> existingEntities = dataAccessService.findSkillsByLeague(league);
    List<SkillEntity> newEntities = safeLines(lines).stream()
        .filter(skill -> existingEntities.stream().noneMatch(entity -> skillEntityMapper.matches(skill, entity)))
        .map(skill -> {
          SkillEntity entity = new SkillEntity();
          entity.setName(skill.getName());
          entity.setVariant(skill.getVariant());
          entity.setGemLevel(skill.getGemLevel());
          entity.setGemQuality(skill.getGemQuality());
          entity.setCorrupted(skill.isCorrupted());
          entity.setChaosEquivalent(skill.getChaosEquivalent());
          entity.setDivineEquivalent(rateService.toDivineEquivalent(skill.getChaosEquivalent(), league));
          entity.setLeague(league);
          return entity;
        })
        .toList();
    dataAccessService.saveSkills(newEntities);
  }
}
