package io.starter.telegram.service;

import io.starter.telegram.dao.SkillsDao;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

  private final AggregatorService aggregatorService;
  private final SkillsDao skillsDAO;

  public DatabaseService(AggregatorService aggregatorService,
                         SkillsDao skillsDAO) {
    this.aggregatorService = aggregatorService;
    this.skillsDAO = skillsDAO;
  }

  public void loadSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(skillsDAO::add);
  }

  @Scheduled(cron = "0 */5 * * * *")
  public void updateSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(skillsDAO::update);
  }

  @Scheduled(cron = "0 */2 * * * *")
  public void addNewSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(skillsDAO::addNew);
  }
}
