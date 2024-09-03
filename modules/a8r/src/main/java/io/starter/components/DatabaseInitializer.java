package io.starter.components;

import io.starter.controller.DatabaseController;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer {

  private final DatabaseController databaseController;

  public DatabaseInitializer(DatabaseController databaseController) {
    this.databaseController = databaseController;
  }

  @PostConstruct
  public void init() {
    databaseController.loadLeagues();
  }
}
