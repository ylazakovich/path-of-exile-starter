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
    aggregatorService.getLeagues().subscribe(leagueDao::saveAll);
  }

  public void loadSkills() {
    leagueDao.readAll().forEach(league -> aggregatorService.getAnalyzedSkills(league.name).subscribe(skillDao::add));
  }

  @Scheduled(cron = "0 */5 * * * *")
  private void updateSkills() {
    leagueDao.readAll().forEach(league -> aggregatorService.getAnalyzedSkills(league.name).subscribe(skillDao::update));
  }

  @Scheduled(cron = "0 */2 * * * *")
  private void addNewSkills() {
    leagueDao.readAll().forEach(league -> aggregatorService.getAnalyzedSkills(league.name).subscribe(skillDao::addNew));
  }
}
