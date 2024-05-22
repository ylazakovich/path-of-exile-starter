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

  public void loadAnalyzedSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(analyzedSkillsDAO::add);
  }

  @Scheduled(cron = "0 */10 * * * *")
  public void refreshAnalyzedSkills() {
    aggregatorService.getAnalyzedSkills().subscribe(analyzedSkillsDAO::update);
  }

  // TODO: Need to create method with adding new positions
}
