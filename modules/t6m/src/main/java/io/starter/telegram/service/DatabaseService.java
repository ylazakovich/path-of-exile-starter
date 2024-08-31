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

  public void loadLeagues() {
    aggregatorService.getLeagues().subscribe(leagueDao::saveIfAbsent);
  }

  public void loadSkills() {
    leagueDao.selectAll()
        .forEach(league -> aggregatorService.getAnalyzedSkills(league.name)
            .subscribe(skills -> skillDao.add(league, skills)));
  }

  @Scheduled(cron = "0 */5 * * * *")
  private void updateSkills() {
    leagueDao.selectAll()
        .forEach(league -> aggregatorService.getAnalyzedSkills(league.name)
            .subscribe(skills -> skillDao.update(league, skills)));
  }

  @Scheduled(cron = "0 */2 * * * *")
  private void addNewSkills() {
    leagueDao.selectAll()
        .forEach(league -> aggregatorService.getAnalyzedSkills(league.name)
            .subscribe(skills -> skillDao.addNew(league, skills)));
  }
}
