package io.starter.controller;

import java.util.List;

import io.starter.entity.LeagueEntity;
import io.starter.service.DataAccessService;
import io.starter.service.PathOfExileService;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leagues")
@Log4j2
public class LeagueController {

  private final DataAccessService dataAccessService;
  private final PathOfExileService pathOfExileService;

  public LeagueController(DataAccessService dataAccessService, PathOfExileService pathOfExileService) {
    this.dataAccessService = dataAccessService;
    this.pathOfExileService = pathOfExileService;
  }

  @GetMapping
  public ResponseEntity<List<LeagueEntity>> getLeagues() {
    return ResponseEntity.ok(dataAccessService.findLeagues());
  }

  @PostMapping("/load")
  public void loadLeagues() {
    pathOfExileService.getAllLeagues().subscribe(response -> {
      dataAccessService.saveLeagues(response.getBody());
      log.info("Loaded {} leagues", dataAccessService.findLeagues().size());
    });
  }
}
