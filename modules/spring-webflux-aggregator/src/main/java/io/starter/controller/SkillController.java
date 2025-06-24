package io.starter.controller;

import io.starter.config.ScheduleConfig;
import io.starter.entity.LeagueEntity;
import io.starter.service.DataAccessService;
import io.starter.service.NinjaDataSyncService;
import io.starter.service.PoeNinjaService;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/skills")
@Log4j2
public class SkillController  {

  private final DataAccessService dataAccessService;
  private final PoeNinjaService poeNinjaService;
  private final NinjaDataSyncService ninjaDataSyncService;

  public SkillController(DataAccessService dataAccessService,
                         PoeNinjaService poeNinjaService,
                         NinjaDataSyncService ninjaDataSyncService) {
    this.dataAccessService = dataAccessService;
    this.poeNinjaService = poeNinjaService;
    this.ninjaDataSyncService = ninjaDataSyncService;
  }

  @PostMapping("/load")
  public void loadSkills() {
    dataAccessService.findLeagues().forEach(this::loadSkills);
  }

  @Scheduled(cron = ScheduleConfig.A8R_UPDATE_CRON)
  public void updateSkills() {
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getSkills(league.getName())
            .subscribe(response -> {
              ninjaDataSyncService.updateSkills(response.getBody(), league);
              log.info("{} - Skill - Schedule updating", league.getName());
            })
    );
  }

  @Scheduled(cron = ScheduleConfig.A8R_ADD_CRON)
  public void addNewSkills() {
    dataAccessService.findLeagues().forEach(league ->
        poeNinjaService.getSkills(league.getName())
            .subscribe(response -> {
              ninjaDataSyncService.addNewSkills(response.getBody(), league);
              log.info("{} - Skill - Schedule adding new", league.getName());
            })
    );
  }

  private void loadSkills(LeagueEntity league) {
    poeNinjaService.getSkills(league.getName())
        .subscribe(response -> {
          ninjaDataSyncService.loadSkills(response.getBody(), league);
          log.info("{} - Skill - Loaded {} units",
              league.getName(),
              dataAccessService.findSkillsByLeague(league).size());
        });
  }
}
