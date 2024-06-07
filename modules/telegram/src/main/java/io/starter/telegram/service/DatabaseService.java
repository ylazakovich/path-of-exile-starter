package io.starter.telegram.service;

import io.starter.telegram.dao.AnalyzedSkillsDao;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

  private final AggregatorService aggregatorService;
  private final AnalyzedSkillsDao analyzedSkillsDAO;

  public DatabaseService(AggregatorService aggregatorService,
                         AnalyzedSkillsDao analyzedSkillsDAO) {
    this.aggregatorService = aggregatorService;
    this.analyzedSkillsDAO = analyzedSkillsDAO;
  }

  public void loadSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(analyzedSkillsDAO::add);
  }

  @Scheduled(cron = "0 */5 * * * *")
  public void updateSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(analyzedSkillsDAO::update);
  }

  @Scheduled(cron = "0 */2 * * * *")
  public void addNewSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(analyzedSkillsDAO::addNew);
  }
}
