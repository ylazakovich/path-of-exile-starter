package io.starter.telegram.service;

import io.starter.telegram.config.ScheduleConfig;
import io.starter.telegram.dao.LeagueDao;
import io.starter.telegram.dao.SkillDao;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

  private static final Logger log = LogManager.getLogger(DatabaseService.class);
  private final A8rService a8rService;
  private final SkillDao skillDao;
  private final LeagueDao leagueDao;

  public DatabaseService(A8rService a8rService,
                         SkillDao skillDao,
                         LeagueDao leagueDao) {
    this.a8rService = a8rService;
    this.skillDao = skillDao;
    this.leagueDao = leagueDao;
  }

  public void loadLeagues() {
    a8rService.getLeagues().subscribe(leagueDao::saveIfAbsent);
  }

  public void loadSkills() {
    leagueDao.selectAll()
        .forEach(league -> {
          log.info("Loading Skills - {} league", league.getName());
          a8rService.getAnalyzedSkills(league.name).subscribe(skills -> skillDao.add(league, skills));
        });
  }

  @Scheduled(cron = ScheduleConfig.T6M_UPDATE_CRON)
  private void updateSkills() {
    leagueDao.selectAll()
        .forEach(league -> a8rService.getAnalyzedSkills(league.name)
            .subscribe(skills -> skillDao.update(league, skills)));
  }

  @Scheduled(cron = ScheduleConfig.T6M_ADD_CRON)
  private void addNewSkills() {
    leagueDao.selectAll()
        .forEach(league -> a8rService.getAnalyzedSkills(league.name)
            .subscribe(skills -> skillDao.addNew(league, skills)));
  }

  @SneakyThrows(InterruptedException.class)
  public void loading() {
    Thread.sleep(2_000);
    loadSkills();
  }
}
