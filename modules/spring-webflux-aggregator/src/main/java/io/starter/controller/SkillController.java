package io.starter.controller;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import io.starter.config.ScheduleConfig;
import io.starter.entity.LeagueEntity;
import io.starter.service.DataAccessService;
import io.starter.service.NinjaDataSyncService;
import io.starter.service.PoeNinjaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
@Log4j2
public class SkillController  {

  private final AtomicBoolean updateSkillsRunning = new AtomicBoolean(false);
  private final AtomicBoolean addNewSkillsRunning = new AtomicBoolean(false);
  private final DataAccessService dataAccessService;
  private final PoeNinjaService poeNinjaService;
  private final NinjaDataSyncService ninjaDataSyncService;

  @PostMapping("/load")
  public void loadSkills() {
    dataAccessService.findLeagues().forEach(this::loadSkills);
  }

  @Scheduled(cron = ScheduleConfig.A8R_SKILLS_UPDATE_CRON)
  public void updateSkills() {
    runScheduledJob("skills-update", updateSkillsRunning, () ->
        Flux.fromIterable(dataAccessService.findLeagues())
            .concatMap(this::updateSkillsForLeague)
            .then());
  }

  @Scheduled(cron = ScheduleConfig.A8R_SKILLS_ADD_CRON)
  public void addNewSkills() {
    runScheduledJob("skills-add-new", addNewSkillsRunning, () ->
        Flux.fromIterable(dataAccessService.findLeagues())
            .concatMap(this::addNewSkillsForLeague)
            .then());
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

  private Mono<Void> updateSkillsForLeague(LeagueEntity league) {
    return poeNinjaService.getSkills(league.getName())
        .doOnNext(response -> {
          ninjaDataSyncService.updateSkills(response.getBody(), league);
          log.info("{} - Skill - Schedule updating", league.getName());
        })
        .then()
        .onErrorResume(e -> {
          log.error("{} - Skill - Schedule updating failed", league.getName(), e);
          return Mono.empty();
        });
  }

  private Mono<Void> addNewSkillsForLeague(LeagueEntity league) {
    return poeNinjaService.getSkills(league.getName())
        .doOnNext(response -> {
          ninjaDataSyncService.addNewSkills(response.getBody(), league);
          log.info("{} - Skill - Schedule adding new", league.getName());
        })
        .then()
        .onErrorResume(e -> {
          log.error("{} - Skill - Schedule adding new failed", league.getName(), e);
          return Mono.empty();
        });
  }

  private void runScheduledJob(String jobName, AtomicBoolean lock, Supplier<Mono<Void>> scheduledAction) {
    if (!lock.compareAndSet(false, true)) {
      log.warn("Skipping '{}' run because previous execution is still in progress", jobName);
      return;
    }
    scheduledAction.get()
        .doFinally(signalType -> lock.set(false))
        .subscribe(
            unused -> { },
            error -> log.error("Job '{}' failed", jobName, error)
        );
  }
}
