package io.starter.controller;

import java.util.concurrent.atomic.AtomicBoolean;

import io.starter.config.ScheduleConfig;
import io.starter.entity.LeagueEntity;
import io.starter.service.DataAccessService;
import io.starter.service.SkillDeltaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/processed-skills")
@RequiredArgsConstructor
@Log4j2
public class ProcessedSkillController {

  private final AtomicBoolean addProcessedSkillsRunning = new AtomicBoolean(false);
  private final AtomicBoolean updateProcessedSkillsRunning = new AtomicBoolean(false);
  private final DataAccessService dataAccessService;
  private final SkillDeltaService skillDeltaService;

  @PostMapping("/load")
  public void loadProcessedSkills() {
    dataAccessService.findLeagues().forEach(league -> {
      dataAccessService.addProcessedSkills(league, skillDeltaService.analyzeSkills(league.getName()));
      log.info("{} - Processed Skill - Processed {} units",
          league.getName(),
          dataAccessService.findProcessedSkillsByLeague(league).size());
    });
  }

  @Scheduled(cron = ScheduleConfig.A8R_PROCESSED_SKILLS_ADD_CRON)
  private void addNewProcessedSkills() {
    runScheduledJob("processed-skills-add-new", addProcessedSkillsRunning, () ->
        dataAccessService.findLeagues().forEach(this::addNewProcessedSkillsForLeague));
  }

  @Scheduled(cron = ScheduleConfig.A8R_PROCESSED_SKILLS_UPDATE_CRON)
  private void updateProcessedSkills() {
    runScheduledJob("processed-skills-update", updateProcessedSkillsRunning, () ->
        dataAccessService.findLeagues().forEach(this::updateProcessedSkillsForLeague));
  }

  private void addNewProcessedSkillsForLeague(LeagueEntity league) {
    try {
      dataAccessService.addNewProcessedSkill(league, skillDeltaService.analyzeSkills(league.getName()));
    } catch (Exception e) {
      log.error("{} - Processed Skill - Add new failed", league.getName(), e);
    }
  }

  private void updateProcessedSkillsForLeague(LeagueEntity league) {
    try {
      dataAccessService.updateProcessedSkills(league, skillDeltaService.analyzeSkills(league.getName()));
    } catch (Exception e) {
      log.error("{} - Processed Skill - Update failed", league.getName(), e);
    }
  }

  private void runScheduledJob(String jobName, AtomicBoolean lock, Runnable scheduledAction) {
    if (!lock.compareAndSet(false, true)) {
      log.warn("Skipping '{}' run because previous execution is still in progress", jobName);
      return;
    }
    try {
      scheduledAction.run();
    } finally {
      lock.set(false);
    }
  }
}
