package io.starter.component;

import io.starter.config.ApplicationConfiguration;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aeonbits.owner.ConfigFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class InitLoader implements ApplicationRunner {

  private final StartupLoader startupLoader;
  private static final ApplicationConfiguration CONFIG =
      ConfigFactory.create(ApplicationConfiguration.class, System.getProperties());

  @Override
  public void run(ApplicationArguments args) {
    if (CONFIG.startupLoaderEnabled()) {
      try {
        log.info("🚀 Starting full init...");
        startupLoader.loadEverything();
        log.info("✅ Full init completed.");
      } catch (InterruptedException e) {
        log.error("Init loading interrupted", e);
        Thread.currentThread().interrupt();
      }
    }
  }
}
