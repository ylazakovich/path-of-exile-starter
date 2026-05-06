package io.starter.service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.starter.dao.RatesDao;
import io.starter.dao.UniqueJewelsDao;
import io.starter.entity.LeagueEntity;
import io.starter.entity.RateEntity;
import io.starter.entity.SkillEntity;
import io.starter.entity.UniqueJewelEntity;
import io.starter.mapper.SkillEntityMapper;
import io.starter.mapper.UniqueJewelMapper;
import io.starter.model.ninja.Currency;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;
import io.starter.model.ninja.UniqueJewel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NinjaDataSyncService {

  private final UniqueJewelsDao uniqueJewelsDao;
  private final RatesDao ratesDao;
  private final DataAccessService dataAccessService;
  private final RateService rateService;
  private final SkillEntityMapper skillEntityMapper;
  private final UniqueJewelMapper uniqueJewelMapper;

  private <T> List<T> safeLines(Lines<T> lines) {
    return (lines != null && lines.getLines() != null)
        ? lines.getLines()
        : List.of();
  }

  public void loadCurrency(Lines<Currency> lines, LeagueEntity league) {
    upsertRates(lines, league);
  }

  public void loadUniqueJewels(Lines<UniqueJewel> lines, LeagueEntity league) {
    List<UniqueJewel> jewelLines = safeLines(lines);
    if (dataAccessService.findUniqueJewelsByLeague(league).isEmpty() && !jewelLines.isEmpty()) {
      uniqueJewelsDao.saveAll(lines, league.getId());
    }
  }

  public void loadSkills(Lines<Skill> lines, LeagueEntity league) {
    List<Skill> skills = safeLines(lines);
    if (dataAccessService.findSkillsByLeague(league).isEmpty() && !skills.isEmpty()) {
      dataAccessService.findLeagueById(league.getId())
          .ifPresent(leagueEntity -> {
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
    upsertRates(lines, league);
  }

  private void upsertRates(Lines<Currency> lines, LeagueEntity league) {
    Map<String, Double> incomingRates = toRatesByName(lines);
    if (incomingRates.isEmpty()) {
      return;
    }

    List<RateEntity> entitiesOnUpdate = dataAccessService.findRatesByLeague(league);
    entitiesOnUpdate.forEach(entity -> {
      Double updatedValue = incomingRates.remove(entity.getName());
      if (updatedValue != null) {
        entity.setChaosEquivalent(updatedValue);
      }
    });
    if (!entitiesOnUpdate.isEmpty()) {
      ratesDao.saveAll(entitiesOnUpdate, league.getId());
    }

    if (!incomingRates.isEmpty()) {
      List<RateEntity> newEntities = incomingRates.entrySet().stream()
          .map(rate -> new RateEntity(rate.getKey(), rate.getValue()))
          .toList();
      ratesDao.saveAll(newEntities, league.getId());
    }
  }

  private Map<String, Double> toRatesByName(Lines<Currency> lines) {
    Map<String, Double> rates = new LinkedHashMap<>();
    safeLines(lines).forEach(currency -> {
      if (currency == null || currency.getName() == null || currency.getName().isBlank()) {
        return;
      }
      if (currency.getChaosEquivalent() <= 0) {
        return;
      }
      rates.merge(currency.getName(), currency.getChaosEquivalent(), Math::min);
    });
    return rates;
  }

  public void updateSkills(Lines<Skill> lines, LeagueEntity league) {
    List<Skill> skillLines = safeLines(lines);
    List<SkillEntity> entitiesOnUpdate = dataAccessService.findSkillsByLeague(league);
    entitiesOnUpdate.forEach(entity -> skillLines.stream()
        .filter(skill -> skillEntityMapper.matches(skill, entity))
        .findFirst()
        .ifPresent(skill -> entity.setChaosEquivalent(skill.getChaosEquivalent()))
    );
    dataAccessService.findLeagueById(league.getId())
        .ifPresent(leagueEntity -> {
          entitiesOnUpdate.forEach(entity -> {
            entity.setLeague(leagueEntity);
            entity.setDivineEquivalent(rateService.toDivineEquivalent(entity.getChaosEquivalent(), league));
          });
          dataAccessService.saveSkills(entitiesOnUpdate);
        });
  }

  public void updateJewels(Lines<UniqueJewel> lines, LeagueEntity league) {
    List<UniqueJewel> jewelsLines = safeLines(lines);
    List<UniqueJewelEntity> entitiesOnUpdate = dataAccessService.findUniqueJewelsByLeague(league);
    entitiesOnUpdate.forEach(entity -> jewelsLines.stream()
        .filter(jewel -> uniqueJewelMapper.matches(jewel, entity))
        .findFirst()
        .ifPresent(jewel -> entity.setChaosEquivalent(jewel.getChaosEquivalent()))
    );
    dataAccessService.findLeagueById(league.getId())
        .ifPresent(leagueEntity -> {
          entitiesOnUpdate.forEach(entity -> entity.setLeague(leagueEntity));
          dataAccessService.saveUniqueJewels(entitiesOnUpdate);
        });

  }

  public void addNewSkills(Lines<Skill> lines, LeagueEntity league) {
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

  public void addNewJewels(Lines<UniqueJewel> lines, LeagueEntity league) {
    List<UniqueJewelEntity> existingEntities = dataAccessService.findUniqueJewelsByLeague(league);
    List<UniqueJewel> uniqueByNameAndLinks = safeLines(lines).stream()
        .filter(jewel -> jewel.getName() != null && !jewel.getName().isBlank())
        .collect(Collectors.toMap(
            this::uniqueKey,
            Function.identity(),
            (left, right) -> Comparator.comparingDouble(UniqueJewel::getChaosEquivalent).compare(left, right) <= 0 ? left : right,
            java.util.LinkedHashMap::new
        ))
        .values()
        .stream()
        .toList();
    List<UniqueJewelEntity> newEntities = uniqueByNameAndLinks.stream()
        .filter(jewel -> existingEntities.stream().noneMatch(entity -> uniqueJewelMapper.matches(jewel, entity)))
        .map(jewel -> {
          UniqueJewelEntity entity = new UniqueJewelEntity();
          entity.setName(jewel.getName());
          entity.setLinks(jewel.getLinks());
          entity.setChaosEquivalent(jewel.getChaosEquivalent());
          entity.setLeague(league);
          return entity;
        })
        .toList();
    dataAccessService.saveUniqueJewels(newEntities);
  }

  private String uniqueKey(UniqueJewel jewel) {
    return jewel.getName() + "|" + (jewel.getLinks() == null ? "null" : jewel.getLinks());
  }
}
