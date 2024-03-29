package io.starter.telegram.service;

import io.starter.telegram.dao.AnalyzedSkillsDAO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

  private final AggregatorService aggregatorService;
  private final AnalyzedSkillsDAO analyzedSkillsDAO;

  public DatabaseService(AggregatorService aggregatorService, AnalyzedSkillsDAO analyzedSkillsDAO) {
    this.aggregatorService = aggregatorService;
    this.analyzedSkillsDAO = analyzedSkillsDAO;
  }

  @Scheduled(cron = "* */10 * * * *")
  public void loadSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(analyzedSkillsDAO::updateAll);
  }
}
