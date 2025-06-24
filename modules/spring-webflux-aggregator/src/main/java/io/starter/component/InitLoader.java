package io.starter.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class InitLoader implements ApplicationRunner {

  private final StartupLoader startupLoader;

  @Override
  public void run(ApplicationArguments args) {
    try {
      startupLoader.loadEverything();
    } catch (InterruptedException e) {
      log.error("Init loading interrupted", e);
      Thread.currentThread().interrupt();
    }
  }
}
