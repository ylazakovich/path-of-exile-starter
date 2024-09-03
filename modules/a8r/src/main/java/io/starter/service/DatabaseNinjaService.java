package io.starter.service;

import java.util.ArrayList;
import java.util.List;

import io.starter.dao.RatesDao;
import io.starter.dao.SkillsDao;
import io.starter.entity.LeagueEntity;
import io.starter.entity.RateEntity;
import io.starter.entity.SkillEntity;
import io.starter.model.ninja.Currency;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;
import io.starter.repo.RatesRepository;
import io.starter.repo.SkillsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseNinjaService {

  private final SkillsDao skillsDao;
  private final RatesDao ratesDao;
  private final SkillsRepository skillsRepository;
  private final RatesRepository ratesRepository;

  @Autowired
  public DatabaseNinjaService(SkillsDao skillsDao,
                              RatesDao ratesDao,
                              SkillsRepository skillsRepository,
                              RatesRepository ratesRepository) {
    this.skillsDao = skillsDao;
    this.ratesDao = ratesDao;
    this.skillsRepository = skillsRepository;
    this.ratesRepository = ratesRepository;
  }

  public void loadCurrency(Lines<Currency> lines, LeagueEntity league) {
    if (ratesRepository.findAllByLeagueId(league).isEmpty() && !lines.getLines().isEmpty()) {
      ratesDao.saveAll(lines, league.getId());
    }
  }

  public void loadSkills(Lines<Skill> lines, LeagueEntity league) {
    if (skillsRepository.findAllByLeagueId(league).isEmpty() && !lines.getLines().isEmpty()) {
      skillsDao.saveAll(lines, league.getId());
    }
  }

  public void updateCurrencies(Lines<Currency> lines, LeagueEntity league) {
    List<RateEntity> entitiesOnUpdate = ratesRepository.findAllByLeagueId(league);
    entitiesOnUpdate.forEach(entity -> lines.getLines().stream()
        .filter(currency -> currency.getName().equals(entity.getName()))
        .findFirst()
        .ifPresent(currency -> entity.setChaosEquivalent(currency.getChaosEquivalent()))
    );
    ratesDao.saveAll(entitiesOnUpdate, league.getId());
  }

  public void updateSkills(Lines<Skill> lines, LeagueEntity league) {
    List<SkillEntity> entitiesOnUpdate = skillsRepository.findAllByLeagueId(league);
    entitiesOnUpdate.forEach(entity -> lines.getLines().stream()
        .filter(skill -> skill.getName().equals(entity.getName())
            && skill.getGemLevel() == entity.getGemLevel()
            && skill.getGemQuality() == entity.getGemQuality()
            && skill.isCorrupted() == entity.getCorrupted())
        .findFirst()
        .ifPresent(skill -> entity.setChaosEquivalent(skill.getChaosEquivalent()))
    );
    skillsDao.saveAll(entitiesOnUpdate, league.getId());
  }

  public void addNew(Lines<Skill> lines, LeagueEntity league) {
    List<SkillEntity> allEntities = skillsRepository.findAllByLeagueId(league);
    List<SkillEntity> entitiesOnAdding = new ArrayList<>();
    lines.getLines().stream()
        .filter(skill -> allEntities.stream()
            .noneMatch(entity -> entity.getName().equals(skill.getName()) &&
                entity.getGemLevel() == skill.getGemLevel() &&
                entity.getGemQuality() == skill.getGemQuality() &&
                entity.getVariant().equals(skill.getVariant()) &&
                entity.getCorrupted() == skill.isCorrupted()
            ))
        .forEach(skill -> {
          SkillEntity entity = new SkillEntity();
          entity.setName(skill.getName());
          entity.setVariant(skill.getVariant());
          entity.setGemLevel(skill.getGemLevel());
          entity.setGemQuality(skill.getGemQuality());
          entity.setCorrupted(skill.isCorrupted());
          entity.setChaosEquivalent(skill.getChaosEquivalent());
          entitiesOnAdding.add(entity);
        });
    skillsDao.saveAll(entitiesOnAdding, league.getId());
  }
}
