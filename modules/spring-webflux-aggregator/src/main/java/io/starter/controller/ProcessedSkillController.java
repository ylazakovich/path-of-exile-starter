package io.starter.controller;

import io.starter.config.ScheduleConfig;
import io.starter.service.DataAccessService;
import io.starter.service.SkillDeltaService;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/processed-skills")
@Log4j2
public class ProcessedSkillController {

  private final DataAccessService dataAccessService;
  private final SkillDeltaService skillDeltaService;

  public ProcessedSkillController(DataAccessService dataAccessService,
                                  SkillDeltaService skillDeltaService) {
    this.dataAccessService = dataAccessService;
    this.skillDeltaService = skillDeltaService;
  }

  @PostMapping("/load")
  public void loadProcessedSkills() {
    dataAccessService.findLeagues().forEach(league -> {
      dataAccessService.addProcessedSkills(league, skillDeltaService.analyzeSkills(league.getName()));
      log.info("{} - Processed Skill - Processed {} units",
          league.getName(),
          dataAccessService.findProcessedSkillsByLeague(league).size());
    });
  }

  @Scheduled(cron = ScheduleConfig.A8R_ADD_CRON)
  private void addNewProcessedSkills() {
    dataAccessService.findLeagues().forEach(league ->
        dataAccessService.addNewProcessedSkill(league, skillDeltaService.analyzeSkills(league.getName()))
    );
  }

  @Scheduled(cron = ScheduleConfig.A8R_UPDATE_CRON)
  private void updateProcessedSkills() {
    dataAccessService.findLeagues().forEach(league ->
        dataAccessService.updateProcessedSkills(league, skillDeltaService.analyzeSkills(league.getName()))
    );
  }
}
