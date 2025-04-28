package io.starter.telegram.components;

import io.starter.telegram.service.DatabaseService;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

  private final DatabaseService databaseService;

  public DatabaseInitializer(DatabaseService databaseService) {
    this.databaseService = databaseService;
  }

  @PostConstruct
  public void init() {
    databaseService.loadLeagues();
  }
}
