package io.starter.telegram.service;

import io.starter.telegram.dao.SkillsDao;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

  private final AggregatorService aggregatorService;
  private final SkillsDao skillsDao;

  public DatabaseService(AggregatorService aggregatorService,
                         SkillsDao skillsDao) {
    this.aggregatorService = aggregatorService;
    this.skillsDao = skillsDao;
  }

  public void loadSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(skillsDao::add);
  }

  @Scheduled(cron = "0 */5 * * * *")
  public void updateSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(skillsDao::update);
  }

  @Scheduled(cron = "0 */2 * * * *")
  public void addNewSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(skillsDao::addNew);
  }
}
