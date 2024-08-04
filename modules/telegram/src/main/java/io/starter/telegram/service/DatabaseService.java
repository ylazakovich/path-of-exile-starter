package io.starter.telegram.service;

import io.starter.telegram.dao.LeagueDao;
import io.starter.telegram.dao.SkillDao;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

  private final AggregatorService aggregatorService;
  private final SkillDao skillDao;
  private final LeagueDao leagueDao;

  public DatabaseService(AggregatorService aggregatorService,
                         SkillDao skillDao,
                         LeagueDao leagueDao) {
    this.aggregatorService = aggregatorService;
    this.skillDao = skillDao;
    this.leagueDao = leagueDao;
  }

  public void loadSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(skillDao::add);
  }

  public void loadLeagues() {
    aggregatorService.getLeagues().subscribe(leagueDao::update);
  }

  @Scheduled(cron = "0 */5 * * * *")
  public void updateSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(skillDao::update);
  }

  @Scheduled(cron = "0 */2 * * * *")
  public void addNewSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(skillDao::addNew);
  }
}
