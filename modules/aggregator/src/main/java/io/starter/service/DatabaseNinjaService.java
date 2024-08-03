package io.starter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.starter.dao.SkillsDao;
import io.starter.entity.LeagueEntity;
import io.starter.entity.SkillEntity;
import io.starter.model.ninja.Lines;
import io.starter.model.ninja.Skill;
import io.starter.repo.LeaguesRepository;
import io.starter.repo.SkillsRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class DatabaseNinjaService {

  private final SkillsDao skillsDao;
  private final SkillsRepository skillsRepository;
  private final LeaguesRepository leaguesRepository;

  @Autowired
  public DatabaseNinjaService(SkillsDao skillsDao,
                              SkillsRepository skillsRepository,
                              LeaguesRepository leaguesRepository) {
    this.skillsDao = skillsDao;
    this.skillsRepository = skillsRepository;
    this.leaguesRepository = leaguesRepository;
  }

  @Transactional
  public void load(Lines<Skill> lines, LeagueEntity league) {
    if (skillsRepository.findAllByLeagueId(league).isEmpty()) {
      skillsDao.saveAll(lines);
    }
  }

  @Transactional
  public Mono<Void> load2(Lines<Skill> lines, LeagueEntity league) {
    return Mono.fromCallable(() -> {
          leaguesRepository.findById(league.getId())
              .flatMap(entity -> {
                if (skillsRepository.findById(entity.getId()).isEmpty()) {
                  skillsDao.saveAll(lines);
                }
                return Optional.empty();
              });
          return Void.TYPE;
        })
        .subscribeOn(Schedulers.boundedElastic())
        .then();
  }

  public void update(Lines<Skill> lines) {
    List<SkillEntity> entitiesOnUpdate = skillsRepository.findAll();
    entitiesOnUpdate.forEach(entity -> lines.getLines().stream()
        .filter(skill -> skill.getName().equals(entity.getName())
            && skill.getGemLevel() == entity.getGemLevel()
            && skill.getGemQuality() == entity.getGemQuality()
            && skill.isCorrupted() == entity.getCorrupted())
        .findFirst()
        .ifPresent(skill -> entity.setChaosEquivalentPrice(skill.getChaosEquivalentPrice()))
    );
    skillsDao.saveAll(entitiesOnUpdate);
  }

  public void addNew(Lines<Skill> lines) {
    List<SkillEntity> allEntities = skillsRepository.findAll();
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
          entity.setChaosEquivalentPrice(skill.getChaosEquivalentPrice());
          entitiesOnAdding.add(entity);
        });
    skillsDao.saveAll(entitiesOnAdding);
  }
}
